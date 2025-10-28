const BASE_URL = "http://localhost:8083/api/market";

export const latestCoins = async () => {
  const response = await fetch(`${BASE_URL}/latest`);
  if (!response.ok) throw new Error("Failed to fetch coins");
  return await response.json();
};

export const fetchCoins = async () => {
  const response = await fetch(`${BASE_URL}/fetch`);
  if (!response.ok) throw new Error("Failed to fetch coins");
  return await response.json();
};

export const fetchCoinHistory = async (symbol, limit = 100) => {
  const response = await fetch(`${BASE_URL}/history/${symbol}?limit=${limit}`);
  if (!response.ok) throw new Error(`Failed to fetch history for ${symbol}`);
  return await response.json();
};

// For SSE
export const streamUrl = `${BASE_URL}/stream`;
