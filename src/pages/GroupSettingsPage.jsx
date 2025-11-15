import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
// import api from '../api';

const GroupSettingsPage = () => {
  const { groupId } = useParams();
  const navigate = useNavigate();

  // State for the rename form
  const [groupName, setGroupName] = useState("Goa Trip (Current Name)"); // Mock current name

  // State for the add member form
  const [memberEmail, setMemberEmail] = useState("");

  const handleRename = (e) => {
    e.preventDefault();
    // --- REAL API CALL (for later) ---
    // await api.put(`/groups/${groupId}/rename`, { name: groupName });
    // alert("Group renamed!");

    // --- MOCK LOGIC (for now) ---
    console.log("Renaming group to:", groupName);
    alert("Mock: Group renamed!");
  };

  const handleAddMember = (e) => {
    e.preventDefault();
    // --- REAL API CALL (for later) ---
    // This is tricky. We need to get userId from email first.
    // const userList = await api.get('/users');
    // const user = userList.data.find(u => u.email === memberEmail);
    // if (user) {
    //   await api.post(`/groups/${groupId}/add-member/${user.id}`);
    //   alert("Member added!");
    //   setMemberEmail("");
    // } else {
    //   alert("User not found!");
    // }

    // --- MOCK LOGIC (for now) ---
    console.log("Adding member:", memberEmail);
    alert("Mock: Member added!");
    setMemberEmail("");
  };

  const handleDeleteGroup = async () => {
    // This is a destructive action, so we use two confirmations
    if (window.confirm("Are you SURE you want to delete this group? This action cannot be undone.")) {
      if (window.confirm("Second confirmation: This will delete all associated expenses. Proceed?")) {

        // --- REAL API CALL (for later) ---
        // try {
        //   await api.delete(`/groups/${groupId}`);
        //   alert("Group deleted.");
        //   navigate('/'); // Redirect to dashboard
        // } catch (err) {
        //   alert("Failed to delete group.");
        // }

        // --- MOCK LOGIC (for now) ---
        console.log("Deleting group:", groupId);
        alert("Mock: Group deleted!");
        navigate('/'); // Redirect to dashboard
      }
    }
  };

  return (
    <div className="container">
      <button onClick={() => navigate(`/group/${groupId}`)} className="btn" style={{width: 'auto', background: '#6c757d', marginBottom: '20px'}}>
        &larr; Back to Group
      </button>

      <h2>Group Settings for Group {groupId}</h2>

      {/* --- RENAME FORM --- */}
      <div className="card" style={{ marginTop: '20px' }}>
        <h3>Rename Group</h3>
        <form onSubmit={handleRename}>
          <div className="form-group">
            <label htmlFor="groupName">Group Name:</label>
            <input
              type="text"
              id="groupName"
              className="form-input"
              value={groupName}
              onChange={(e) => setGroupName(e.target.value)}
              required
            />
          </div>
          <button type="submit" className="btn" style={{ width: 'auto' }}>
            Save Name
          </button>
        </form>
      </div>

      {/* --- ADD MEMBER FORM --- */}
      <div className="card" style={{ marginTop: '20px' }}>
        <h3>Add Member</h3>
        <form onSubmit={handleAddMember}>
          <div className="form-group">
            <label htmlFor="memberEmail">User Email:</label>
            <input
              type="email"
              id="memberEmail"
              className="form-input"
              value={memberEmail}
              onChange={(e) => setMemberEmail(e.target.value)}
              placeholder="user@example.com"
              required
            />
          </div>
          <button type="submit" className="btn" style={{ width: 'auto' }}>
            Add Member
          </button>
        </form>
      </div>

      {/* --- DELETE GROUP SECTION --- */}
      <div className="card" style={{ marginTop: '20px', borderColor: '#dc3545' }}>
        <h3 style={{ color: '#dc3545' }}>Danger Zone</h3>
        <p>Deleting a group is permanent. It will remove the group and all associated expenses for all members.</p>
        <button 
          className="btn btn-logout" // Re-using the red logout button style
          style={{ width: 'auto' }}
          onClick={handleDeleteGroup}
        >
          Delete This Group
        </button>
      </div>

    </div>
  );
};

export default GroupSettingsPage;