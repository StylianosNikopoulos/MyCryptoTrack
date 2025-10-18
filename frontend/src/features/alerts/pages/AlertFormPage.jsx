import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { createAlert, updateAlert } from "../api/alert.api";

const AlertFormPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { alert: existingAlert, symbol: locationSymbol } = location.state || {};

  const isEditing = Boolean(existingAlert);

  const [symbol, setSymbol] = useState(
    isEditing ? existingAlert.symbol : locationSymbol || ""
  );
  const [targetPrice, setTargetPrice] = useState(
    isEditing ? existingAlert.targetPrice : ""
  );
  const [type, setType] = useState(isEditing ? existingAlert.type : "SELL");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  if (!symbol) return <p>No coin selected</p>;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      if (isEditing) {
        // Update existing alert
        await updateAlert(existingAlert.id, {
          symbol,
          targetPrice: parseFloat(targetPrice),
          type,
        });
      } else {
        // Create new alert
        await createAlert({
          symbol,
          targetPrice: parseFloat(targetPrice),
          type,
        });
      }
      navigate("/alerts");
    } catch (err) {
      console.error(err);
      setError(isEditing ? "Failed to update alert." : "Failed to create alert.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="alert-form-page">
      <form className="alert-form-page__card" onSubmit={handleSubmit}>
        <h2 className="alert-form-page__title">
          {isEditing ? `Update Alert for ${symbol}` : `Create Alert for ${symbol}`}
        </h2>

        <label className="alert-form-page__label">
          Alert Type:
          <select
            className="alert-form-page__select"
            value={type}
            onChange={(e) => setType(e.target.value)}
          >
            <option value="BUY">BUY</option>
            <option value="SELL">SELL</option>
          </select>
        </label>

        <label className="alert-form-page__label">
          Target Price:
          <input
            type="number"
            step="0.01"
            value={targetPrice}
            onChange={(e) => setTargetPrice(e.target.value)}
            className="alert-form-page__input"
            required
          />
        </label>

        {error && <p className="alert-form-page__error">{error}</p>}

        <button
          className="alert-form-page__btn"
          type="submit"
          disabled={loading}
        >
          {loading ? "Saving..." : isEditing ? "Update Alert" : "Save Alert"}
        </button>
      </form>
    </div>
  );
};

export default AlertFormPage;
