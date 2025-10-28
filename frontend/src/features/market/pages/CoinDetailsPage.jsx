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
import { fetchCoins, fetchCoinHistory, streamUrl } from "../api/market.api";

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

  // Fetch coin info + historical data
  useEffect(() => {
    const loadData = async () => {
      try {
        const allCoins = await fetchCoins();
        const selectedCoin = allCoins.find((c) => c.symbol === symbol);
        if (selectedCoin) setCoin(selectedCoin);

        const history = await fetchCoinHistory(symbol, 365); 
        const formattedHistory = history.map((h) => ({
          price: h.price,
          time: new Date(h.fetchedAt),
        }));
        setPriceHistory(formattedHistory);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [symbol]);

  // SSE for live price updates
  useEffect(() => {
    if (!coin) return;

    const source = new EventSource(streamUrl);

    source.onmessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.symbol === coin.symbol) {
        setPriceHistory((prev) => [
          ...prev.slice(-364),
          { price: data.price, time: new Date() },
        ]);
      }
    };

    source.onerror = () => source.close();
    return () => source.close();
  }, [coin]);

  if (loading || !coin) return <p>Loading coin data...</p>;

  // Prepare chart
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
          label: function (context) {
            return `$${context.raw.toLocaleString()}`;
          },
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
