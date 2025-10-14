import React from "react";

const AlertList = ({ alerts, onDelete }) => {
  if (!alerts || alerts.length === 0) return <p>No alerts yet.</p>;

  return (
    <table className="alerts-page__table">
      <thead>
        <tr>
          <th className="alerts-page__th">Symbol</th>
          <th className="alerts-page__th">Current Price</th>
          <th className="alerts-page__th">Target Price</th>
          <th className="alerts-page__th">Type</th>
          <th className="alerts-page__th">Action</th>
        </tr>
      </thead>
      <tbody>
        {alerts.map((a) => (
          <tr className="alerts-page__tr" key={a.id}>
            <td className="alerts-page__td">{a.symbol}</td>
            <td className="alerts-page__td">{a.p}</td>
            <td className="alerts-page__td">${a.targetPrice}</td>
            <td className="alerts-page__td">{a.type}</td>
            <td className="alerts-page__td">
              <button
                className="alerts-page__btn-delete"
                onClick={() => onDelete(a.id)}
              >
                Delete
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default AlertList;
