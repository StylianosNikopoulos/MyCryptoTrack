import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  email: localStorage.getItem("email") || null,
  username: localStorage.getItem("username") || null,
  token: localStorage.getItem("token") || null,
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUser: (state, action) => {
      const { email, username, token } = action.payload;
      state.email = email;
      state.username = username;
      state.token = token;
      if (token) localStorage.setItem("token", token);
      if (email) localStorage.setItem("email", email);
      if (username) localStorage.setItem("username", username);
    },
    logout: (state) => {
      state.email = null;
      state.username = null;
      state.token = null;
      localStorage.clear();
    },
  },
});

export const { setUser, logout } = userSlice.actions;
export default userSlice.reducer;
