import React from 'react';
import { Link, NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

// We'll add the CSS for this in the next step
const Navbar = () => {
  const { logout } = useAuth();

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-logo">
          Fairshare
        </Link>
        <ul className="nav-menu">
          <li className="nav-item">
            <NavLink to="/" className="nav-link" end>
              Dashboard
            </NavLink>
          </li>
          <li className="nav-item">
            <NavLink to="/checklist" className="nav-link">
              My Checklist
            </NavLink>
          </li>
        </ul>
        <button onClick={logout} className="btn btn-logout nav-btn">
          Logout
        </button>
      </div>
    </nav>
  );
};

export default Navbar;