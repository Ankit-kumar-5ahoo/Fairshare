import React, { useState } from 'react';
import Modal from './Modal';

const AddExpenseModal = ({ members, onClose, onAddExpense }) => {
  const [description, setDescription] = useState('');
  const [amount, setAmount] = useState('');
  
  const [paidBy, setPaidBy] = useState(members[0]?.id || '');

  const handleSubmit = (e) => {
    e.preventDefault();
    
   
    const expenseData = {
      description,
      amount: parseFloat(amount),
      paidBy: parseInt(paidBy),
      
    };
    onAddExpense(expenseData);
  };

  return (
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
          {}
          {members.length > 0 ? (
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
          ) : (
            <p>Loading members...</p>
          )}
        </div>
        <button type="submit" className="btn" style={{ marginTop: '10px' }}>
          Add Expense
        </button>
      </form>
    </Modal>
  );
};

export default AddExpenseModal;