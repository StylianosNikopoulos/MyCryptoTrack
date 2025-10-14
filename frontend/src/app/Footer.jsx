import React from "react";
import { Link } from "react-router-dom";
import "../styles/footer.css";

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-left">
          <h2>MyCryptoTrack</h2>
          <p>Real-time crypto market overview & alerts</p>
        </div>

        <div className="footer-right">
          <Link to="/">Home</Link>
          <Link to="/info">Info</Link>
        </div>
      </div>

      <div className="footer-bottom">
        &copy; {new Date().getFullYear()} MyCryptoTrack. All rights reserved.
      </div>
    </footer>
  );
};

export default Footer;
