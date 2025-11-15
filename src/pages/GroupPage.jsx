import React, { useState, useEffect } from 'react';
// 1. Make sure 'Link' is imported
import { useParams, useNavigate, Link } from 'react-router-dom';
import AddExpenseModal from '../components/AddExpenseModal';
import EditExpenseModal from '../components/EditExpenseModal';
import SettleUpModal from '../components/SettleUpModal';
// import api from '../api'; 

// --- MOCK DATA (matches API docs) ---
const mockExpenses = [
  { id: 55, description: "Lunch", amount: 750, paidBy: { id: 1, name: "Ankit" } },
  { id: 56, description: "Taxi", amount: 300, paidBy: { id: 2, name: "Rahul" } },
  { id: 57, description: "Snacks", amount: 120, paidBy: { id: 1, name: "Ankit" } },
];

const mockMembers = [
  { id: 1, name: "Ankit" },
  { id: 2, name: "Rahul" },
  { id: 3, name: "Sana" },
];
// ------------------------------------

const GroupPage = () => {
  const { groupId } = useParams();
  const navigate = useNavigate();

  const [expenses, setExpenses] = useState([]);
  const [members, setMembers] = useState([]);
  const [loading, setLoading] = useState(true);
  
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [editingExpense, setEditingExpense] = useState(null);
  const [isSettleModalOpen, setIsSettleModalOpen] = useState(false);
  
  const groupName = "Goa Trip"; // Mock group name

  useEffect(() => {
    // Simulating data fetch
    setTimeout(() => {
      setExpenses(mockExpenses);
      setMembers(mockMembers);
      setLoading(false);
    }, 500); 
  }, [groupId]);

  // --- Handler Functions ---

  const handleAddExpense = async (expenseData) => {
    // (Logic for later)
    console.log("New expense data:", expenseData);
    setIsAddModalOpen(false);
  };

  const handleEditExpense = async (expenseId, updatedData) => {
    // (Logic for later)
    console.log("Updating expense:", expenseId, updatedData);
    setEditingExpense(null);
  };
  
  const handleDeleteExpense = async (expenseId) => {
    if (window.confirm("Are you sure you want to delete this expense?")) {
      // (Logic for later)
      console.log("Deleting expense:", expenseId);
    }
  };

  // --- Loading State ---

  if (loading) {
    return <div className="container">Loading group details...</div>;
  }

  // --- Render JSX ---

  return (
    <div className="container">
      
      {/* --- Modals (Rendered with full props) --- */}
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

      {/* --- Header --- */}
      <div className="dashboard-header">
        <div>
          <button onClick={() => navigate('/')} className="btn" style={{width: 'auto', background: '#6c757d'}}>
            &larr; Back to Dashboard
          </button>
          <h2 style={{ marginTop: '15px' }}>{groupName}</h2>
        </div>
        {/* --- This is the new Settings Button --- */}
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
      
      {/* --- Action Buttons --- */}
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

      {/* --- Main Content Grid --- */}
      <div className="dashboard-grid">
        
        {/* Expense List Card */}
        <div className="card">
          <h3>Expenses</h3>
          <ul>
            {expenses.map((expense) => (
              <li key={expense.id} className="list-item" style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                <div>
                  <strong>{expense.description}</strong> - ₹{expense.amount}
                  <br />
                  <small>Paid by {expense.paidBy.name}</small>
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
          </ul>
        </div>

        {/* Member List Card */}
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

      </div> {/* End of dashboard-grid */}
    </div> // End of container
  );
};

export default GroupPage;