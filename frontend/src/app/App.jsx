import React from "react";
import { BrowserRouter } from "react-router-dom";
import RoutesConfig from "./routes.jsx";
import Navbar from "../shared/components/Navbar.jsx";
import { Toaster } from "react-hot-toast";
import Footer from "./Footer.jsx";

const App = () => {
  return (
    <BrowserRouter>
      <div className="app-container">
        <Navbar />
        <main className="app-main">
          <RoutesConfig />
        </main>
        <Footer />

        {/* Toasts */}
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 3000,
            style: {
              background: "#1f2937",
              color: "#fff",
              borderRadius: "8px",
              fontSize: "0.95rem",
              boxShadow: "0 4px 12px rgba(0,0,0,0.3)",
            },
            success: {
              iconTheme: { primary: "#4ade80", secondary: "#1f2937" },
            },
            error: {
              iconTheme: { primary: "#f87171", secondary: "#1f2937" },
            },
          }}
        />
      </div>
    </BrowserRouter>
  );
};

export default App;
