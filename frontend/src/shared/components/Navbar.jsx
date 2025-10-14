import React from "react";
import { Link } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { logout } from "../../app/store/slices/userSlice";

const Navbar = () => {
  const dispatch = useDispatch();
  const user = useSelector((state) => state.user); 

  const handleLogout = () => {
    dispatch(logout());
  };

  return (
    <nav className="flex gap-4 p-4 bg-gray-900 text-white">
      <Link to="/">Market</Link>

      {user?.token && <Link to="/alerts">Alerts</Link>}

      {user?.token ? (
        <button onClick={handleLogout} className="px-3 py-1 rounded bg-red-600">
          Logout
        </button>
      ) : (
        <>
          <Link to="/login">Login</Link>
          <Link to="/register">Register</Link>
        </>
      )}
    </nav>
  );
};

export default Navbar;
