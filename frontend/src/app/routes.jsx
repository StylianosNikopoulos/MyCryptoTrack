import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import { useSelector } from "react-redux";
import LoginPage from "../features/auth/pages/LoginPage.jsx";
import RegisterPage from "../features/auth/pages/RegisterPage.jsx";
import MarketOverviewPage from "../features/market/pages/MarketOverviewPage.jsx";
import AlertPage from "../features/alerts/pages/AlertPage.jsx";

const RoutesConfig = () => {
  const user = useSelector((state) => state.user);
  const isAuthenticated = !!user?.token; 

  return (
    <Routes>
      <Route path="/" element={<MarketOverviewPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />

      <Route
        path="/alerts"
        element={
          isAuthenticated ? <AlertPage /> : <Navigate to="/login" replace />
        }
      />
    </Routes>
  );
};

export default RoutesConfig;
