import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import CreateGroupModal from '../components/CreateGroupModal';
import api from '../api';

const DashboardPage = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const fetchData = async () => {
    try {
      const response = await api.get('/dashboard/me');
      setData(response.data);
    } catch (error) {
      console.error('Failed to fetch dashboard data', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    setLoading(true);
    fetchData();
  }, []);

  const handleCreateGroup = async (groupData) => {
    const { name, memberEmails } = groupData;
    const params = new URLSearchParams();
    params.append('name', name);

    try {
      const response = await api.post(`/groups/create?${params.toString()}`, memberEmails);
      const newGroup = response.data;

      
      // It will be handles the case where 'groups' doesn't exist yet -- fix by aryan
      setData(prevData => ({
        ...prevData,
        groups: [...(prevData.groups || []), newGroup]
      }));
      
      setIsModalOpen(false);

    } catch (error) {
      console.error('Failed to create group', error);
      alert('Failed to create group. Please try again.');
    }
  };

  if (loading) {
    return <div className="container">Loading dashboard...</div>;
  }

  if (!data) {
    return <div className="container">Error loading data. Please try logging out and back in.</div>;
  }

  return (
    <div className="container">
      
      {isModalOpen && (
        <CreateGroupModal
          onClose={() => setIsModalOpen(false)}
          onCreateGroup={handleCreateGroup}
        />
      )}

      <div className="dashboard-header">
        <h2>Welcome, {data.user?.name || 'User'}!</h2>
      </div>

      <div className="card" style={{ marginTop: '20px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <h3>Your Groups</h3>
          <button 
            className="btn" 
            style={{ width: 'auto' }}
            onClick={() => setIsModalOpen(true)}
          >
            + Create Group
          </button>
        </div>
        <ul>
          {data.groups?.map((group) => (
            <li key={group.id}>
              <Link to={`/group/${group.id}`} className="list-item-link">
                {group.name}
              </Link>
            </li>
          ))}
          {(data.groups?.length || 0) === 0 && (
            <li className="list-item">You are not in any groups yet.</li>
          )}
        </ul>
      </div>

      <div className="dashboard-grid">
        <div className="card">
          <h3>Recent Expenses</h3>
          <ul>
            {data.recentExpenses?.map((expense) => (
              <li key={expense.id} className="list-item">
                {expense.description} - <strong>₹{expense.amount}</strong>
              </li>
            ))}
            {(data.recentExpenses?.length || 0) === 0 && (
              <li className="list-item">No recent expenses.</li>
            )}
          </ul>
        </div>
        <div className="card">
          <h3>Recent Logs</h3>
          <ul>
            {data.recentLogs?.map((log, index) => (
              <li key={index} className="list-item" style={{ fontSize: '14px' }}>
                {log}
              </li>
            ))}
            {(data.recentLogs?.length || 0) === 0 && (
              <li className="list-item">No recent activity.</li>
            )}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;