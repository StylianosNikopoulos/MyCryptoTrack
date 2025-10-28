import React, { useState, useEffect } from "react";
import { getAllAlerts, deleteAlert } from "../api/alert.api";
import { fetchCoins, streamUrl } from "../../market/api/market.api"; 
import AlertList from "../components/AlertList";
import { useNavigate } from "react-router-dom";
import ConfirmModal from "../components/ConfirmModal";

const AlertsPage = () => {
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [confirmDeleteId, setConfirmDeleteId] = useState(null);
  const navigate = useNavigate();

  const fetchAlertsWithPrices = async () => {
    try {
      const [alertsData, marketData] = await Promise.all([
        getAllAlerts(),
        fetchCoins(),
      ]);

      const enriched = alertsData.map((a) => {
        const coin = marketData.find((c) => c.symbol === a.symbol);
        return {
          ...a,
          currentPrice: coin ? coin.price : "N/A",
        };
      });

      setAlerts(enriched);
    } catch (err) {
      console.error("Error fetching alerts or market data:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteAlert(id);
      setAlerts((prev) => prev.filter((a) => a.id !== id));
      setConfirmDeleteId(null);
    } catch (err) {
      console.error(err);
      setConfirmDeleteId(null);
      alert("Failed to delete alert.");
    }
  };

  const handleUpdate = (alert) => {
    navigate("/alerts/update", { state: { alert } });
  };

  useEffect(() => {
    fetchAlertsWithPrices();
  }, []);

  // ✅ Optional: live updates with SSE
  useEffect(() => {
    const source = new EventSource(streamUrl);
    source.onmessage = (event) => {
      const data = JSON.parse(event.data);
      setAlerts((prev) =>
        prev.map((a) =>
          a.symbol === data.symbol
            ? { ...a, currentPrice: data.price }
            : a
        )
      );
    };
    source.onerror = () => source.close();
    return () => source.close();
  }, []);

  if (loading) return <p>Loading alerts...</p>;

  return (
    <div className="alerts-page">
      <h1 className="alerts-page__title">My Alerts</h1>

      <AlertList
        alerts={alerts}
        onDelete={(id) => setConfirmDeleteId(id)}
        onUpdate={handleUpdate}
      />

      {confirmDeleteId && (
        <ConfirmModal
          title="Delete Alert"
          message="Are you sure you want to delete this alert?"
          onConfirm={() => handleDelete(confirmDeleteId)}
          onCancel={() => setConfirmDeleteId(null)}
          confirmText="Delete"
          cancelText="Cancel"
        />
      )}
    </div>
  );
};

export default AlertsPage;
