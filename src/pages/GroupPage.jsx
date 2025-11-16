import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import AddExpenseModal from '../components/AddExpenseModal';
import EditExpenseModal from '../components/EditExpenseModal';
import SettleUpModal from '../components/SettleUpModal';
import api from '../api';

const GroupPage = () => {
  const { groupId } = useParams();
  const navigate = useNavigate();

  const [group, setGroup] = useState(null);
  const [expenses, setExpenses] = useState([]);
  const [members, setMembers] = useState([]);
  const [loading, setLoading] = useState(true);
  
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [editingExpense, setEditingExpense] = useState(null);
  const [isSettleModalOpen, setIsSettleModalOpen] = useState(false);

  
  const fetchData = async () => {
    try {
      setLoading(true);
      const [groupRes, membersRes, expensesRes] = await Promise.all([
        api.get(`/groups/${groupId}`),
        api.get(`/groups/${groupId}/members`),
        api.get(`/expenses/group/${groupId}`)
      ]);
      
      setGroup(groupRes.data);
      setMembers(membersRes.data);
      setExpenses(expensesRes.data);

    } catch (error) {
      console.error("Failed to fetch group data", error);
      alert("Failed to load group data. You may not be a member of this group.");
      navigate('/'); 
    } finally {
      setLoading(false);
    }
  };

  
  useEffect(() => {
    fetchData();
    
  }, [groupId, navigate]);

  const handleAddExpense = async (expenseData) => {
    try {
      const dataToSend = { ...expenseData, groupId: parseInt(groupId) };
      await api.post('/expenses', dataToSend);
      setIsAddModalOpen(false);
      fetchData(); 
    } catch (error) {
      console.error("Failed to add expense", error);
      alert("Failed to add expense.");
    }
  };

  
  const handleEditExpense = async (expenseId, updatedData) => {
    try {
      
      const dataToSend = { ...updatedData, groupId: parseInt(groupId) };
      
      await api.put(`/expenses/${expenseId}`, dataToSend);
      setEditingExpense(null);
      fetchData(); 
    } catch (error) {
      console.error("Failed to update expense", error);
      alert("Failed to update expense.");
    }
  };
  
  const handleDeleteExpense = async (expenseId) => {
    if (window.confirm("Are you sure you want to delete this expense?")) {
      try {
        await api.delete(`/expenses/${expenseId}`);
        fetchData(); 
      } catch (error) {
        console.error("Failed to delete expense", error);
        alert("Failed to delete expense.");
      }
    }
  };

  if (loading) {
    return <div className="container">Loading group details...</div>;
  }

  return (
    <div className="container">
      
      {isAddModalOpen && (
        <AddExpenseModal
          members={members}
          onClose={() => setIsAddModalOpen(false)}
          onAddExpense={handleAddExpense}
        />
      )}

      {editingExpense && (
        <EditExpenseModal
          expenseToEdit={editingExpense}
          members={members}
          onClose={() => setEditingExpense(null)}
          onEditExpense={handleEditExpense}
        />
      )}

      {isSettleModalOpen && (
        <SettleUpModal
          onClose={() => setIsSettleModalOpen(false)}
          groupId={groupId}
        />
      )}

      <div className="dashboard-header">
        <div>
          <button onClick={() => navigate('/')} className="btn" style={{width: 'auto', background: '#6c757d'}}>
            &larr; Back to Dashboard
          </button>
          <h2 style={{ marginTop: '15px' }}>{group?.name || 'Group'}</h2>
        </div>
        <div>
          <Link 
            to={`/group/${groupId}/settings`} 
            className="btn"
            style={{ width: 'auto', background: '#555' }}
          >
            Group Settings
          </Link>
        </div>
      </div>
      
      <div style={{ margin: '20px 0' }}>
        <button 
          className="btn" 
          style={{ width: 'auto', marginRight: '10px' }}
          onClick={() => setIsAddModalOpen(true)}
        >
          Add Expense
        </button>
        <button 
          className="btn" 
          style={{ width: 'auto', background: '#28a745' }}
          onClick={() => setIsSettleModalOpen(true)}
        >
          Settle Up
        </button>
      </div>

      <div className="dashboard-grid">
        <div className="card">
          <h3>Expenses</h3>
          <ul>
            {expenses.map((expense) => (
              <li key={expense.id} className="list-item" style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                <div>
                  <strong>{expense.description}</strong> - ₹{expense.amount}
                  <br />
                  <small>Paid by {expense.paidBy?.name || '...'}</small>
                </div>
                <div>
                  <button 
                    style={{ fontSize: '12px', marginRight: '5px' }}
                    onClick={() => setEditingExpense(expense)}
                  >
                    Edit
                  </button>
                  <button 
                    style={{ fontSize: '12px' }}
                    onClick={() => handleDeleteExpense(expense.id)}
                  >
                    Delete
                  </button>
                </div>
              </li>
            ))}
            {expenses.length === 0 && (
              <li className="list-item">No expenses added yet.</li>
            )}
          </ul>
        </div>

        <div className="card">
          <h3>Members</h3>
          <ul>
            {members.map((member) => (
              <li key={member.id} className="list-item">
                {member.name}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}; 
export default GroupPage;