import React, { useEffect, useState } from "react";
import {
  fetchNotifications,
  deleteNotification,
} from "../api/notifications.api";

const NotificationsPage = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await fetchNotifications();
        setNotifications(data);
      } catch (err) {
        console.error("Error loading notifications:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const handleDelete = async (id) => {
    try {
      await deleteNotification(id);
      setNotifications((prev) => prev.filter((n) => n.id !== id));
    } catch (err) {
      console.error("Error deleting notification:", err);
    }
  };

  if (loading)
    return <p style={{ textAlign: "center" }}>Loading notifications...</p>;

  return (
    <div className="notifications-page">
      <h1>Your Notifications</h1>

      {notifications.length === 0 ? (
        <p className="empty-state">No notifications found.</p>
      ) : (
        <ul className="notifications-list">
          {notifications.map((n) => (
            <li
              key={n.id}
              style={{
                background: n.read ? "#f6f6f6" : "#fffbe6",
                padding: "10px",
                marginBottom: "6px",
                borderRadius: "8px",
              }}
            >
              <div>
                <p>{n.message}</p>
                <small>{new Date(n.createdAt).toLocaleString()}</small>
              </div>
              <div style={{ marginTop: "4px" }}>
                <button
                  onClick={() => handleDelete(n.id)}
                  style={{ marginLeft: "8px" }}
                >
                  Delete
                </button>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default NotificationsPage;
