import React, { useEffect, useState } from "react";
import NotificationDropdown from "./NotificationDropdown";

const NotificationBell = () => {
  const [notifications, setNotifications] = useState([]);
  const [isOpen, setIsOpen] = useState(false);

  useEffect(() => {
    const timer = setInterval(() => {
      if (Math.random() < 0.2) {
        setNotifications((prev) => [
          {
            id: Date.now(),
            message: "BTC price reached your target!",
            time: new Date().toLocaleTimeString(),
          },
          ...prev,
        ]);
      }
    }, 8000);
    return () => clearInterval(timer);
  }, []);

  const handleToggle = () => setIsOpen(!isOpen);
  const handleDelete = (id) =>
    setNotifications((prev) => prev.filter((n) => n.id !== id));
  const handleClearAll = () => setNotifications([]);

  return (
    <div className="notification-bell">
      <button className="bell-btn" onClick={handleToggle}>
        🔔
        {notifications.length > 0 && (
          <span className="badge">{notifications.length}</span>
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
