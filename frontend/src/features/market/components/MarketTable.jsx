import React, { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";

const MarketTable = ({ data, rowsPerPage = 10 }) => {
  const [currentPage, setCurrentPage] = useState(1);
  const [coinStatus, setCoinStatus] = useState(() => {
    const saved = localStorage.getItem("coinStatus");
    return saved ? JSON.parse(saved) : {};
  });
  const [pinnedCoins, setPinnedCoins] = useState(() => {
    const saved = localStorage.getItem("pinnedCoins");
    return saved ? JSON.parse(saved) : [];
  });
  const [sortOrder, setSortOrder] = useState(null);
  const prevPricesRef = useRef({});
  const navigate = useNavigate();

  // Track previous prices for up/down
  useEffect(() => {
    const newStatus = { ...coinStatus };
    data.forEach((coin) => {
      const prev = prevPricesRef.current[coin.symbol];
      const diff = prev !== undefined ? coin.price - prev : 0;
      if (diff > 0) newStatus[coin.symbol] = "up";
      else if (diff < 0) newStatus[coin.symbol] = "down";
      prevPricesRef.current[coin.symbol] = coin.price;
    });
    setCoinStatus(newStatus);
    localStorage.setItem("coinStatus", JSON.stringify(newStatus));
  }, [data]);

  // Favourite
  const togglePin = (symbol) => {
    const updated = pinnedCoins.includes(symbol)
      ? pinnedCoins.filter((s) => s !== symbol)
      : [symbol, ...pinnedCoins];
    setPinnedCoins(updated);
    localStorage.setItem("pinnedCoins", JSON.stringify(updated));
  };

  // Filter and sort
  let filteredData = data.filter(
    (coin) =>
      coin.price &&
      coin.price > 0.001 &&
      coin.symbol &&
      (coin.symbol.endsWith("USD") || coin.symbol.endsWith("USDT"))
  );

  filteredData.sort((a, b) => {
    const aPinned = pinnedCoins.includes(a.symbol) ? 1 : 0;
    const bPinned = pinnedCoins.includes(b.symbol) ? 1 : 0;
    if (aPinned !== bPinned) return bPinned - aPinned;
    if (sortOrder === "asc") return a.price - b.price;
    if (sortOrder === "desc") return b.price - a.price;
    return 0;
  });

  const totalPages = Math.ceil(filteredData.length / rowsPerPage);
  const startIndex = (currentPage - 1) * rowsPerPage;
  const currentData = filteredData.slice(startIndex, startIndex + rowsPerPage);

  const goToPage = (page) => {
    if (page < 1 || page > totalPages) return;
    setCurrentPage(page);
  };

  const toggleSort = () => {
    if (sortOrder === "asc") setSortOrder("desc");
    else if (sortOrder === "desc") setSortOrder(null);
    else setSortOrder("asc");
  };

  const renderArrow = () => {
    if (sortOrder === "asc") return "▲";
    if (sortOrder === "desc") return "▼";
    return "↕";
  };

  return (
    <div className="market-table-wrapper">
      <table className="market-table">
        <thead>
          <tr>
            <th>Pin</th>
            <th>Symbol</th>
            <th>
              <button className="sort-button" onClick={toggleSort}>
                Price (USD) {renderArrow()}
              </button>
            </th>
            <th>Details</th>
          </tr>
        </thead>
        <tbody>
          {currentData.map((coin) => {
            const status = coinStatus[coin.symbol] || "neutral";
            const priceClass =
              status === "up"
                ? "price-up"
                : status === "down"
                ? "price-down"
                : "price-neutral";
            const isPinned = pinnedCoins.includes(coin.symbol);

            return (
              <tr key={coin.symbol}>
                <td>
                  <span
                    className={`pin-icon ${isPinned ? "pinned" : ""}`}
                    onClick={() => togglePin(coin.symbol)}
                  >
                    ★
                  </span>
                </td>
                <td>{coin.symbol}</td>
                <td className={`price-change ${priceClass}`}>
                  ${coin.price.toFixed(2)}
                </td>
                <td>
                <button
                  type="button" 
                  className="btn-primary" 
                  onClick={() => {
                    sessionStorage.setItem("selectedCoin", JSON.stringify(coin));
                    navigate("/coin/" + coin.symbol);
                  }}
                >
                  View Chart
                </button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>

      {/* Pagination */}
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
