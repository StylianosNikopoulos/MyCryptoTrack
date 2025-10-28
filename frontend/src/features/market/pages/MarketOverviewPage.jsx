import React, { useEffect, useState, useCallback } from "react";
import { latestCoins, fetchCoins, streamUrl } from "../api/market.api";
import MarketTable from "../components/MarketTable";

const MarketOverviewPage = () => {
  const [coins, setCoins] = useState([]);
  const [lastUpdated, setLastUpdated] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [error, setError] = useState(null);

  // Load cached data
  const loadCachedData = useCallback(async () => {
    try {
      const data = await latestCoins();
      setCoins(data);
      setError(null);
    } catch (err) {
      console.error("Cached data failed:", err);
      setError("Could not load cached data");
    }
  }, []);

  // Fetch live data
  const refreshLiveData = useCallback(async () => {
    try {
      const freshData = await fetchCoins();
      setCoins(freshData);
      setLastUpdated(new Date());
    } catch (err) {
      console.warn("Live refresh failed:", err);
    }
  }, []);

  // Initial load + interval
  useEffect(() => {
    let isMounted = true;
    (async () => {
      await loadCachedData();
      await refreshLiveData();
    })();

    const interval = setInterval(() => {
      if (isMounted) refreshLiveData();
    }, 60_000);

    return () => {
      isMounted = false;
      clearInterval(interval);
    };
  }, [loadCachedData, refreshLiveData]);

  // SSE for real-time updates
  useEffect(() => {
    const source = new EventSource(streamUrl);

    source.onmessage = (event) => {
      const data = JSON.parse(event.data);
      setCoins((prev) => {
        const updated = [...prev];
        const index = updated.findIndex((c) => c.symbol === data.symbol);
        if (index >= 0) updated[index] = data;
        else updated.push(data);
        return updated;
      });
    };

    source.onerror = (error) => {
      console.error("SSE error:", error);
      source.close();
    };

    return () => source.close();
  }, []);

  const filteredCoins = coins.filter(
    (coin) =>
      !searchTerm ||
      coin.symbol.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="market-page">
      <div className="market-header">
        <h1>Market Overview</h1>
        {/* Search Bar */}
        <input
          type="text"
          placeholder="Search coin by symbol..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="market-search"
        />
      </div>

      {error ? (
        <p style={{ color: "#f87171" }}>{error}</p>
      ) : (
        <div className="market-card">
          <MarketTable data={filteredCoins} />
        </div>
      )}
    </div>
  );
};

export default MarketOverviewPage;
