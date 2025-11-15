import React, { useState } from 'react';
import Modal from './Modal';

const CreateGroupModal = ({ onClose, onCreateGroup }) => {

  const [name, setName] = useState('');
  const [memberEmails, setMemberEmails] = useState(''); // We'll use a single text area

  const handleSubmit = (e) => {
    e.preventDefault();

    // Split the emails by comma or newline and trim whitespace
    const emailsArray = memberEmails
      .split(/[\n,]+/) // Split by newline or comma
      .map(email => email.trim()) // Remove whitespace
      .filter(email => email.length > 0); // Remove empty strings

    // --- REAL API CALL (for later) ---
    // const groupData = {
    //   name,
    //   memberEmails: emailsArray,
    // };
    // onCreateGroup(groupData);

    // --- MOCK LOGIC (for now) ---
    console.log('Creating group:', {
      name,
      emailsArray,
    });
    onClose(); // Just close the modal on submit
  };

  return (
    <Modal onClose={onClose}>
      <h2>Create New Group</h2>
      <form onSubmit={handleSubmit}>

        <div className="form-group">
          <label htmlFor="name">Group Name:</label>
          <input
            type="text"
            id="name"
            className="form-input"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="memberEmails">Member Emails:</label>
          <textarea
            id="memberEmails"
            className="form-input"
            value={memberEmails}
            onChange={(e) => setMemberEmails(e.target.value)}
            rows="4"
            placeholder="Enter emails, separated by commas or new lines"
          />
          <small>You are automatically added to the group.</small>
        </div>

        <button type="submit" className="btn" style={{ marginTop: '10px' }}>
          Create Group
        </button>
      </form>
    </Modal>
  );
};

export default CreateGroupModal;