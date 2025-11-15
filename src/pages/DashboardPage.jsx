import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { Link } from 'react-router-dom';
// --- 1. IMPORT THE NEW MODAL ---
import CreateGroupModal from '../components/CreateGroupModal';
// import api from '../api';

// (Mock data is the same)
const mockDashboardData = {
  user: { id: 1, name: 'Mock User', email: 'mock@user.com' },
  groups: [
    { id: 12, name: 'Goa Trip' },
    { id: 7, name: 'Roommates' },
    { id: 15, name: 'Office Lunch' },
  ],
  recentExpenses: [/* ... */],
  recentLogs: [/* ... */],
};

const DashboardPage = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  // --- 2. ADD STATE FOR THE MODAL ---
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    setTimeout(() => {
      setData(mockDashboardData);
      setLoading(false);
    }, 500);
  }, []);

  // --- 3. ADD HANDLER FOR CREATING A GROUP ---
  const handleCreateGroup = async (groupData) => {
    // (Logic for later: await api.post('/api/groups/create', groupData))
    console.log("New group data:", groupData);
    setIsModalOpen(false);
    // After API call, we would refetch dashboard data
  };

  if (loading) {
    return <div className="container">Loading dashboard...</div>;
  }

  if (!data) {
    return <div className="container">Error loading data.</div>;
  }

  return (
    <div className="container">

      {/* --- 4. RENDER THE MODAL (if open) --- */}
      {isModalOpen && (
        <CreateGroupModal
          onClose={() => setIsModalOpen(false)}
          onCreateGroup={handleCreateGroup}
        />
      )}

      <div className="dashboard-header">
        <h2>Welcome, {data.user.name}!</h2>
      </div>

      <div className="card" style={{ marginTop: '20px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <h3>Your Groups</h3>
          {/* --- 5. WIRE UP THE "CREATE GROUP" BUTTON --- */}
          <button 
            className="btn" 
            style={{ width: 'auto' }}
            onClick={() => setIsModalOpen(true)}
          >
            + Create Group
          </button>
        </div>
        <ul>
          {data.groups.map((group) => (
            <li key={group.id}>
              <Link to={`/group/${group.id}`} className="list-item-link">
                {group.name}
              </Link>
            </li>
          ))}
        </ul>
      </div>

      {/* (Rest of the page is the same) */}
      <div className="dashboard-grid">
        <div className="card">
          <h3>Recent Expenses</h3>
          <ul>
            {data.recentExpenses.map((expense) => (
              <li key={expense.id} className="list-item">
                {expense.description} - <strong>₹{expense.amount}</strong>
              </li>
            ))}
          </ul>
        </div>
        <div className="card">
          <h3>Recent Logs</h3>
          <ul>
            {data.recentLogs.map((log, index) => (
              <li key={index} className="list-item" style={{ fontSize: '14px' }}>
                {log}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;