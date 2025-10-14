import React, { useEffect, useState, useCallback } from "react";
import { latestCoins, fetchCoins, streamUrl } from "../api/market.api";
import MarketTable from "../components/MarketTable";

const MarketOverviewPage = () => {
  const [coins, setCoins] = useState([]);
  const [lastUpdated, setLastUpdated] = useState(null);
  const [refreshing, setRefreshing] = useState(false);
  const [error, setError] = useState(null);

 // 1: Load cached data (from DB)
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

   // 2: Fetch live data (from Bin)
  const refreshLiveData = useCallback(async () => {
    try {
      setRefreshing(true);
      const freshData = await fetchCoins();
      setCoins(freshData);
      setLastUpdated(new Date());
    } catch (err) {
      console.warn("Live refresh failed:", err);
    } finally {
      setRefreshing(false);
    }
  }, []);

  // 3: Load once, then refresh every minute
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

  // SSE: real-time updates with auto-reconnect
  useEffect(() => {
    const source = new EventSource(streamUrl);

    source.onmessage = (event) => {
      const data = JSON.parse(event.data);
      setCoins((prev) => {
        const updated = [...prev];
        const index = updated.findIndex((c) => c.symbol === data.symbol);
        if (index >= 0) updated[index] = data;
        else updated.push(data);
        return [...updated];
      });
    };

    source.onerror = (error) => {
      console.error("SSE error:", error);
      source.close();
    };

    return () => source.close();
  }, []);

  return (
    <div className="p-4">
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-semibold">Market Overview</h1>
        <div className="flex items-center gap-3">
          {lastUpdated && (
            <span className="text-sm text-gray-500">
              Updated at {lastUpdated.toLocaleTimeString()}
            </span>
          )}
          <button
            onClick={refreshLiveData}
            disabled={refreshing}
            className={`px-3 py-1 rounded ${
              refreshing ? "bg-gray-300" : "bg-blue-600 text-white"
            }`}
          >
            {refreshing ? "Refreshing..." : "Refresh"}
          </button>
        </div>
      </div>
      {error ? <p className="text-red-500">{error}</p> : <MarketTable data={coins} />}
    </div>
  );
};

export default MarketOverviewPage;
