import React from "react";

const NotificationDropdown = ({
  notifications,
  onDelete,
  onClearAll,
  onMarkAsRead,
}) => {
  return (
    <div className="notification-dropdown">
      <div className="dropdown-header">
        <h4>Notifications</h4>
        {notifications.length > 0 && (
          <button className="clear-btn" onClick={onClearAll}>
            Clear All
          </button>
        )}
      </div>

      {notifications.length === 0 ? (
        <p className="no-notifs">No new notifications</p>
      ) : (
        <ul>
          {notifications.map((n) => (
            <li
              key={n.id}
              className={n.read ? "notif-read" : "notif-unread"}
              style={{
                backgroundColor: n.read ? "#f4f4f4" : "#fffbe6",
                borderBottom: "1px solid #ddd",
                padding: "8px",
              }}
            >
              <div>
                <p>{n.message}</p>
                <span style={{ fontSize: "0.8em", color: "#777" }}>
                  {new Date(n.createdAt).toLocaleTimeString()}
                </span>
              </div>
              <div style={{ display: "flex", gap: "6px" }}>
                {!n.read && (
                  <button onClick={() => onMarkAsRead(n.id)}>✔️</button>
                )}
                <button onClick={() => onDelete(n.id)}>✖</button>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default NotificationDropdown;
