import React, { useEffect, useState } from "react";
import { Line } from "react-chartjs-2";
import { useNavigate, useParams } from "react-router-dom";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { fetchCoins, streamUrl } from "../api/market.api";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const CoinDetailsPage = () => {
  const { symbol } = useParams();
  const navigate = useNavigate();
  const [coin, setCoin] = useState(null);
  const [priceHistory, setPriceHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [range, setRange] = useState("1y");

  const rangeOptions = {
    "1m": 30,
    "3m": 90,
    "6m": 180,
    "1y": 365,
    all: 1000,
  };

  const BIN_BASE_URL = "https://api.binance.com/api/v3/klines";

  // Fetch coin info + historical data
  useEffect(() => {
    const loadData = async () => {
      if (!symbol) return;
      setLoading(true);
      try {
        const allCoins = await fetchCoins();
        const selectedCoin = allCoins.find((c) => c.symbol === symbol);
        if (selectedCoin) setCoin(selectedCoin);

        const limit = rangeOptions[range] || 365;

        const url = `${BIN_BASE_URL}?symbol=${symbol}&interval=1d&limit=${limit}`;

        const res = await fetch(url);
        const data = await res.json();

        const formattedHistory = data.map((kline) => ({
          price: parseFloat(kline[4]),
          time: new Date(kline[0]),
        }));

        setPriceHistory(formattedHistory);
      } catch (err) {
        console.error("Error fetching Binance data:", err);
      } finally {
        setLoading(false);
      }
    };
    loadData();
  }, [symbol, range]);

  // SSE for live price updates
  useEffect(() => {
    if (!coin) return;
    const source = new EventSource(streamUrl);

    source.onmessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.symbol === coin.symbol) {
        setPriceHistory((prev) => [
          ...prev.slice(-rangeOptions[range]),
          { price: data.price, time: new Date() },
        ]);
      }
    };

    source.onerror = () => source.close();
    return () => source.close();
  }, [coin, range]);

  if (loading || !coin) return <p>Loading coin data...</p>;

  const chartData = {
    labels: priceHistory.map((p) =>
      p.time.toLocaleDateString("en-US", { month: "short", day: "numeric" })
    ),
    datasets: [
      {
        label: coin.symbol,
        data: priceHistory.map((p) => p.price),
        borderColor: "#6366f1",
        backgroundColor: "rgba(99,102,241,0.2)",
        tension: 0.3,
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: { display: true },
      tooltip: {
        callbacks: {
          label: (context) => `$${context.raw.toLocaleString()}`,
        },
      },
    },
    scales: {
      x: { display: true },
      y: { display: true },
    },
  };

  return (
    <div className="coin-details-page">
      <h1>{coin.symbol} Chart</h1>

      {/* Range Selector */}
      <div className="range-selector">
        {Object.keys(rangeOptions).map((r) => (
          <button
            key={r}
            className={range === r ? "active-range" : ""}
            onClick={() => setRange(r)}
          >
            {r.toUpperCase()}
          </button>
        ))}
      </div>

      <button
        onClick={() => {
          sessionStorage.setItem("selectedCoin", JSON.stringify(coin));
          navigate("/alerts/create", {
            state: { symbol: coin.symbol, price: coin.price },
          });
        }}
      >
        Create Alert
      </button>

      <div className="chart-container">
        <Line data={chartData} options={options} />
      </div>
    </div>
  );
};

export default CoinDetailsPage;
