import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const MarketTable = ({ data, rowsPerPage = 10 }) => {
  const [currentPage, setCurrentPage] = useState(1);
  const navigate = useNavigate(); 

  const filteredData = data.filter(
    (coin) =>
      coin.price &&
      coin.price > 0.001 &&
      coin.symbol &&
      coin.symbol.endsWith("USD")
  );

  const totalPages = Math.ceil(filteredData.length / rowsPerPage);
  const startIndex = (currentPage - 1) * rowsPerPage;
  const currentData = filteredData.slice(startIndex, startIndex + rowsPerPage);

  const goToPage = (page) => {
    if (page < 1 || page > totalPages) return;
    setCurrentPage(page);
  };

  return (
    <div className="market-table-wrapper">
      <table className="market-table">
        <thead>
          <tr>
            <th>Symbol</th>
            <th>Price (USD)</th>
            <th>Alert</th>
          </tr>
        </thead>
        <tbody>
          {currentData.map((coin) => (
            <tr key={coin.id || coin.symbol}>
              <td>{coin.symbol}</td>
              <td>${coin.price.toFixed(2)}</td>
              <td>
                <button
                  className="btn-primary"
                  onClick={() =>
                    navigate("/alerts/create", { state: { symbol: coin.symbol, price: coin.price } })
                  }
                >
                  Create Alert
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {totalPages > 1 && (
        <div className="pagination">
          <button
            onClick={() => goToPage(currentPage - 1)}
            disabled={currentPage === 1}
          >
            Prev
          </button>

          {currentPage > 2 && (
            <>
              <button onClick={() => goToPage(1)}>1</button>
              {currentPage > 3 && <span className="dots">...</span>}
            </>
          )}

          <button className="active">{currentPage}</button>

          {currentPage < totalPages - 1 && (
            <>
              {currentPage < totalPages - 2 && <span className="dots">...</span>}
              <button onClick={() => goToPage(totalPages)}>{totalPages}</button>
            </>
          )}

          <button
            onClick={() => goToPage(currentPage + 1)}
            disabled={currentPage === totalPages}
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
};

export default MarketTable;
