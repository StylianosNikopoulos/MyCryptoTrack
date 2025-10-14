const BASE_URL = "http://localhost:8081/api/auth";

export const login = async (email, password) => {
  const response = await fetch(`${BASE_URL}/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email, password }),
  });

  if (!response.ok) {
    // const errorText = await response.text();
    throw new Error(`Login failed`);
  }

  return await response.text();
};

export const register = async ({ username, email, password }) => {
  const response = await fetch(`${BASE_URL}/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, email, password }),
  });

  if (!response.ok) {
    // const errorText = await response.text();
    throw new Error(`Register failed`);
  }

  return await response.text();
};
