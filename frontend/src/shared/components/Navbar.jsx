import React from "react";
import { Link } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { logout } from "../../app/store/slices/userSlice";
import "../../styles/navbar.css"; 

const Navbar = () => {
  const dispatch = useDispatch();
  const user = useSelector((state) => state.user);

  const handleLogout = () => {
    dispatch(logout());
  };

  return (
    <nav className="navbar">
      <div className="nav-links">
        <Link to="/">Market</Link>
        {user?.token && <Link to="/alerts">Alerts</Link>}
      </div>

      <div className="nav-actions">
        {user?.token ? (
          <button className="logout-btn" onClick={handleLogout}>Logout</button>
        ) : (
          <>
            <Link to="/login">Login</Link>
            <Link to="/register">Register</Link>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
