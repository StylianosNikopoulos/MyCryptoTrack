import React from "react";
import { BrowserRouter } from "react-router-dom";
import RoutesConfig from "./routes.jsx";
import Navbar from "../shared/components/Navbar.jsx";
import { Toaster } from "react-hot-toast";

const App = () => {
  return (
    <BrowserRouter>
      <Navbar />
      <RoutesConfig />
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 3000,
          style: {
            background: "#333",
            color: "#be1212ff",
            borderRadius: "8px",
            fontSize: "0.95rem",
          },
          success: {
            iconTheme: {
              primary: "#4ade80", 
              secondary: "#333",
            },
          },
          error: {
            iconTheme: {
              primary: "#f87171", 
              secondary: "#333",
            },
          },
        }}
      />
    </BrowserRouter>
  );
};

export default App;
