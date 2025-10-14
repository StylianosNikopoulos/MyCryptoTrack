import React, { useState, useEffect } from "react";
import { getAllAlerts, deleteAlert } from "../api/alert.api";
import AlertList from "../components/AlertList";

const AlertsPage = () => {
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchAlerts = async () => {
    try {
      const data = await getAllAlerts();
      setAlerts(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteAlert(id);
      setAlerts((prev) => prev.filter((a) => a.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchAlerts();
  }, []);

  if (loading) return <p>Loading alerts...</p>;

  return (
    <div className="alerts-page">
      <h1 className="alerts-page__title">My Alerts</h1>
      <AlertList alerts={alerts} onDelete={handleDelete} />
    </div>
  );
};

export default AlertsPage;
