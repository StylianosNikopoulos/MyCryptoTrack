import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import { useSelector } from "react-redux";
import LoginPage from "../features/auth/pages/LoginPage.jsx";
import RegisterPage from "../features/auth/pages/RegisterPage.jsx";
import MarketOverviewPage from "../features/market/pages/MarketOverviewPage.jsx";
import AlertsPage from "../features/alerts/pages/AlertsPage.jsx";
import AlertFormPage from "../features/alerts/pages/AlertFormPage.jsx";
import CoinDetailsPage from "../features/market/pages/CoinDetailsPage.jsx";
import NotificationsPage from "../features/notifications/pages/NotificationsPage.jsx";

const RoutesConfig = () => {
  const user = useSelector((state) => state.user);
  const isAuthenticated = !!user?.token;

  return (
    <Routes>
      <Route path="/" element={<MarketOverviewPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />

      <Route
        path="/notifications"
        element={isAuthenticated ? <NotificationsPage /> : <Navigate to="/login" replace />}
      />
      
      <Route
        path="/alerts"
        element={isAuthenticated ? <AlertsPage /> : <Navigate to="/login" replace />}
      />

      <Route
        path="/alerts/create"
        element={isAuthenticated ? <AlertFormPage /> : <Navigate to="/login" replace />}
      />

      <Route
        path="/alerts/update"
        element={isAuthenticated ? <AlertFormPage /> : <Navigate to="/login" replace />}
      />
      <Route path="/coin/:symbol" element={<CoinDetailsPage />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
};

export default RoutesConfig;
