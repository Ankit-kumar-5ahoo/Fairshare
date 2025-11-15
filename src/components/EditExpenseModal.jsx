import React, { useState } from 'react';
import Modal from './Modal';

// 'expenseToEdit' is the object with {id, description, amount, paidBy}
// 'members' is the full list of members for the dropdown
const EditExpenseModal = ({ expenseToEdit, members, onClose, onEditExpense }) => {

  // 1. Pre-fill the form state with the existing expense data
  const [description, setDescription] = useState(expenseToEdit.description);
  const [amount, setAmount] = useState(expenseToEdit.amount);
  const [paidBy, setPaidBy] = useState(expenseToEdit.paidBy.id);

  const handleSubmit = (e) => {
    e.preventDefault();

    // --- REAL API CALL (for later) ---
    // const updatedExpenseData = {
    //   description,
    //   amount: parseFloat(amount),
    //   paidBy: parseInt(paidBy),
    // };
    // onEditExpense(expenseToEdit.id, updatedExpenseData);

    // --- MOCK LOGIC (for now) ---
    console.log('Editing expense:', expenseToEdit.id, {
      description,
      amount,
      paidBy,
    });
    onClose(); // Just close the modal on submit
  };

  return (
    <Modal onClose={onClose}>
      <h2>Edit Expense</h2>
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
            {members.map((member) => (
              <option key={member.id} value={member.id}>
                {member.name}
              </option>
            ))}
          </select>
        </div>

        <button type="submit" className="btn" style={{ marginTop: '10px' }}>
          Save Changes
        </button>
      </form>
    </Modal>
  );
};

export default EditExpenseModal;