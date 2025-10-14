import React from "react";

const MarketTable = ({ data }) => {
const filteredData = data.filter(
  (coin) =>
    coin.price && coin.price > 0.001 &&
    coin.symbol && coin.symbol.endsWith("USD")
);


  return (
    <table className="min-w-full border-collapse border border-gray-300">
      <thead>
        <tr className="bg-gray-100">
          <th className="border p-2 text-left">Symbol</th>
          <th className="border p-2 text-right">Price</th>
        </tr>
      </thead>
      <tbody>
        {filteredData.map((coin) => (
          <tr key={coin.id || coin.symbol}>
            <td className="border p-2">{coin.symbol}</td>
            <td className="border p-2 text-right">
              {coin.price.toFixed(2)} {}
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default MarketTable;
