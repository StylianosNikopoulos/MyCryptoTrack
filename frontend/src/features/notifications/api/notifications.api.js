const BASE_URL = "http://localhost:8082/api/notifications";

const getAuthHeaders = () => {
  const token = localStorage.getItem("token"); 
  return {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${token}`,
  };
};

export const fetchNotifications = async () => {
  const res = await fetch(BASE_URL, { headers: getAuthHeaders() });
  if (!res.ok) throw new Error("Failed to fetch notifications");
  return await res.json();
};

export const deleteNotification = async (id) => {
  const res = await fetch(`${BASE_URL}/${id}`, { method: "DELETE", headers: getAuthHeaders() });
  if (!res.ok) throw new Error("Failed to delete notification");
  return true;
};
