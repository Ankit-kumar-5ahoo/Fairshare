import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api'; 

const GroupSettingsPage = () => {
  const { groupId } = useParams();
  const navigate = useNavigate();
  
  
  const [groupName, setGroupName] = useState(""); 
  
  
  const [memberEmail, setMemberEmail] = useState("");
  const [loading, setLoading] = useState(true);

  
  useEffect(() => {
    const fetchGroup = async () => {
      try {
        const res = await api.get(`/groups/${groupId}`);
        setGroupName(res.data.name);
      } catch (err) {
        console.error("Failed to fetch group", err);
        alert("Failed to load group. You may not be a member.");
        navigate('/');
      } finally {
        setLoading(false);
      }
    };
    fetchGroup();
  }, [groupId, navigate]);

  const handleRename = async (e) => {
    e.preventDefault();
    try {
      const params = new URLSearchParams();
      params.append('newName', groupName);
      
      await api.put(`/groups/${groupId}/rename?${params.toString()}`);
      alert("Group renamed successfully!");
    } catch (err) {
      console.error("Failed to rename group", err);
      alert("Failed to rename group. Only the group creator can do this.");
    }
  };

  const handleAddMember = async (e) => {
    e.preventDefault();
    try {
      
      
      const userListRes = await api.get('/users');
      
      
      const userToAdd = userListRes.data.find(user => user.email === memberEmail);

      if (!userToAdd) {
        alert("Error: User with that email not found in the database.");
        return;
      }

      
      await api.post(`/groups/${groupId}/add-member/${userToAdd.id}`);
      alert("Member added successfully!");
      setMemberEmail(""); 
    } catch (err) {
      console.error("Failed to add member", err);
      alert("Failed to add member. They might already be in the group.");
    }
  };

  const handleDeleteGroup = async () => {
    
    if (window.confirm("Are you SURE you want to delete this group? This action cannot be undone.")) {
      if (window.confirm("Second confirmation: This will delete all associated expenses. Proceed?")) {
        try {
          await api.delete(`/groups/${groupId}`);
          alert("Group deleted.");
          navigate('/');
        } catch (err) {
          console.error("Failed to delete group", err);
          alert("Failed to delete group. Only the group creator can do this.");
        }
      }
    }
  };

  if (loading) {
    return <div className="container">Loading settings...</div>;
  }

  return (
    <div className="container">
      <button onClick={() => navigate(`/group/${groupId}`)} className="btn" style={{width: 'auto', background: '#6c757d', marginBottom: '20px'}}>
        &larr; Back to Group
      </button>

      <h2>Group Settings for {groupName}</h2>

      {}
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

      {}
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

      {}
      <div className="card" style={{ marginTop: '20px', borderColor: '#dc3545' }}>
        <h3 style={{ color: '#dc3545' }}>Danger Zone</h3>
        <p>Deleting a group is permanent. It will remove the group and all associated expenses for all members.</p>
        <button 
          className="btn btn-logout" 
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