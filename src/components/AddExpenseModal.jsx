import React, { useState } from 'react';
import Modal from './Modal'; // Import our reusable wrapper

// We'll receive 'members' as a prop to build the dropdown
// 'onClose' and 'onAddExpense' are functions passed from GroupPage
const AddExpenseModal = ({ members, onClose, onAddExpense }) => {

  // State for the form fields
  const [description, setDescription] = useState('');
  const [amount, setAmount] = useState('');
  // Set the "paid by" to the first member by default
  const [paidBy, setPaidBy] = useState(members[0]?.id || '');

  const handleSubmit = (e) => {
    e.preventDefault();

    // --- REAL API CALL (for later) ---
    // const expenseData = {
    //   description,
    //   amount: parseFloat(amount),
    //   groupId: /* we'll need to pass this in */,
    //   paidBy: parseInt(paidBy),
    // };
    // onAddExpense(expenseData);

    // --- MOCK LOGIC (for now) ---
    console.log('Adding expense:', {
      description,
      amount,
      paidBy,
    });
    onClose(); // Just close the modal on submit
  };

  return (
    // Use our reusable Modal component
    <Modal onClose={onClose}>
      <h2>Add New Expense</h2>
      <form onSubmit={handleSubmit}>

        <div className="form-group">
          <label htmlFor="description">Description:</label>
          <input
            type="text"
            id="description"
            className="form-input"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="amount">Amount (₹):</label>
          <input
            type="number"
            id="amount"
            className="form-input"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            required
            min="0.01"
            step="0.01"
          />
        </div>

        <div className="form-group">
          <label htmlFor="paidBy">Paid by:</label>
          <select
            id="paidBy"
            className="form-input"
            value={paidBy}
            onChange={(e) => setPaidBy(e.target.value)}
          >
            {/* Map over the members to create dropdown options */}
            {members.map((member) => (
              <option key={member.id} value={member.id}>
                {member.name}
              </option>
            ))}
          </select>
        </div>

        <button type="submit" className="btn" style={{ marginTop: '10px' }}>
          Add Expense
        </button>
      </form>
    </Modal>
  );
};

export default AddExpenseModal;