import React, { useState } from 'react';
import Modal from './Modal';


const CreateGroupModal = ({ onClose, onCreateGroup }) => {
  const [name, setName] = useState('');
  const [memberEmails, setMemberEmails] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    
    
    const emailsArray = memberEmails
      .split(/[\n,]+/) 
      .map(email => email.trim())
      .filter(email => email.length > 0);

    
    const groupData = {
      name,
      memberEmails: emailsArray,
    };
    
    
    onCreateGroup(groupData);
    
   
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
          <small>Your own email is automatically added on the backend.</small>
        </div>

        <button type="submit" className="btn" style={{ marginTop: '10px' }}>
          Create Group
        </button>
      </form>
    </Modal>
  );
};

export default CreateGroupModal;