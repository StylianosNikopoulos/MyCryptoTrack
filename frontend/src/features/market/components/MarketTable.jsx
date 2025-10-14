import React from "react";

const MarketTable = ({ data }) => {
  const filteredData = data.filter(
    (coin) => coin.price && coin.price > 0.001 && coin.symbol && coin.symbol.endsWith("USD")
  );

  return (
    <table>
      <thead>
        <tr>
          <th>Symbol</th>
          <th style={{ textAlign: "right" }}>Price</th>
        </tr>
      </thead>
      <tbody>
        {filteredData.map((coin) => (
          <tr key={coin.id || coin.symbol}>
            <td>{coin.symbol}</td>
            <td style={{ textAlign: "right" }}>{coin.price.toFixed(2)}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default MarketTable;
