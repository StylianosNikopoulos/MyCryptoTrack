import React from "react";

const NotificationDropdown = ({ notifications, onDelete, onClearAll }) => {
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
            <li key={n.id}>
              <div>
                <p>{n.message}</p>
                <span>{n.time}</span>
              </div>
              <button onClick={() => onDelete(n.id)}>✖</button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default NotificationDropdown;
