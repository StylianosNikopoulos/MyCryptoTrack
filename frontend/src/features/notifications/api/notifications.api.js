const BASE_URL = "http://localhost:8083/api/notifications";

export const fetchNotifications = async () => {
  const res = await fetch(BASE_URL);
  if (!res.ok) throw new Error("Failed to fetch notifications");
  return await res.json();
};

export const deleteNotification = async (id) => {
  const res = await fetch(`${BASE_URL}/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Failed to delete notification");
  return await res.json();
};

export const markAsRead = async (id) => {
  const res = await fetch(`${BASE_URL}/${id}/read`, { method: "PATCH" });
  if (!res.ok) throw new Error("Failed to mark as read");
  return await res.json();
};
