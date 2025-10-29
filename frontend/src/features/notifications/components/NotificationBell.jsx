import React, { useEffect, useState } from "react";
import NotificationDropdown from "./NotificationDropdown";
import {
  fetchNotifications,
  deleteNotification,
} from "../api/notifications.api";

const NotificationBell = () => {
  const [notifications, setNotifications] = useState([]);
  const [isOpen, setIsOpen] = useState(false);

  useEffect(() => {
    const loadNotifications = async () => {
      try {
        const data = await fetchNotifications();
        setNotifications(data);
      } catch (err) {
        console.error("Error fetching notifications:", err);
      }
    };

    loadNotifications();

    // refresh notifications every 30s
    const interval = setInterval(loadNotifications, 30000);
    return () => clearInterval(interval);
  }, []);

  const handleToggle = () => setIsOpen(!isOpen);

  const handleDelete = async (id) => {
    try {
      await deleteNotification(id);
      setNotifications((prev) => prev.filter((n) => n.id !== id));
    } catch (err) {
      console.error("Error deleting notification:", err);
    }
  };

  const handleClearAll = () => setNotifications([]);

  return (
    <div className="notification-bell">
      <button className="bell-btn" onClick={handleToggle}>
        🔔
        {notifications.filter((n) => !n.read).length > 0 && (
          <span className="badge">
            {notifications.filter((n) => !n.read).length}
          </span>
        )}
      </button>

      {isOpen && (
        <NotificationDropdown
          notifications={notifications}
          onDelete={handleDelete}
          onClearAll={handleClearAll}
        />
      )}
    </div>
  );
};

export default NotificationBell;
