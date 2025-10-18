const BASE_URL = "http://localhost:8082/api/alert";

const getAuthHeader = () => {
  const token = localStorage.getItem("token");
  return token ? { Authorization: `Bearer ${token}` } : {};
};

export const createAlert = async ({ symbol, targetPrice, type }) => {
  const response = await fetch(`${BASE_URL}/create`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      ...getAuthHeader(),
    },
    body: JSON.stringify({ symbol, targetPrice, type }),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`Failed to create alert: ${errorText}`);
  }

  return response.json();
};

export const updateAlert = async (id, { symbol, targetPrice, type }) => {
  const response = await fetch(`${BASE_URL}/update/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      ...getAuthHeader(),
    },
    body: JSON.stringify({ symbol, targetPrice, type }),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`Failed to update alert: ${errorText}`);
  }

  return response.json();
};

export const getAllAlerts = async () => {
  const response = await fetch(`${BASE_URL}/allAlerts`, {
    headers: {
      ...getAuthHeader(),
    },
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`Failed to fetch alerts: ${errorText}`);
  }

  return response.json();
};

export const deleteAlert = async (id) => {
  const response = await fetch(`${BASE_URL}/${id}`, {
    method: "DELETE",
    headers: {
      ...getAuthHeader(),
    },
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`Failed to delete alert: ${errorText}`);
  }
};
