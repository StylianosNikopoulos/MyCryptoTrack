import React, { useState, useEffect } from "react";
import { getAllAlerts, deleteAlert } from "../api/alert.api";
import AlertList from "../components/AlertList";
import { useNavigate } from "react-router-dom";
import ConfirmModal from "../components/ConfirmModal";

const AlertsPage = () => {
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [confirmDeleteId, setConfirmDeleteId] = useState(null); 
  const navigate = useNavigate();

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
    fetchAlerts();
  }, []);

  if (loading) return <p>Loading alerts...</p>;

  return (
    <div className="alerts-page">
      <h1 className="alerts-page__title">My Alerts</h1>

      {/* Alert table */}
      <AlertList
        alerts={alerts}
        onDelete={(id) => setConfirmDeleteId(id)} 
        onUpdate={handleUpdate}
      />

      {/* Confirmation modal */}
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
