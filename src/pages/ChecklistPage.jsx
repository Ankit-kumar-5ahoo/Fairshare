import React, { useState, useEffect } from 'react';
import api from '../api'; 

const ChecklistPage = () => {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  
  
  const [description, setDescription] = useState("");

  
  const fetchItems = async () => {
    try {
      const res = await api.get('/checklist'); 
      setItems(res.data);
    } catch (err) {
      console.error("Failed to fetch checklist", err);
    } finally {
      setLoading(false);
    }
  };

  
  useEffect(() => {
    fetchItems();
  }, []);

  
  const handleAddItem = async (e) => {
    e.preventDefault();
    try {
      
      const res = await api.post('/checklist', { description }); 
      setItems([...items, res.data]);
      setDescription(""); 
    } catch (err) {
      console.error("Failed to add item", err);
      alert("Failed to add item."); 
    }
  };

  
  const handleToggleItem = async (id) => {
    try {
      const res = await api.put(`/checklist/${id}/toggle`);
      setItems(items.map(item => (item.id === id ? res.data : item)));
    } catch (err) {
      console.error("Failed to toggle item", err);
    }
  };

  
  const handleDeleteItem = async (id) => {
    if (window.confirm("Delete this item?")) {
      try {
        await api.delete(`/checklist/${id}`);
        setItems(items.filter(item => item.id !== id));
      } catch (err) {
        console.error("Failed to delete item", err);
        alert("Failed to delete item.");
      }
    }
  };

  return (
    <div className="container">
      <h2>My Personal Checklist</h2>
      <p>This list is private and not shared with any group.</p>

      <div className="card" style={{ marginBottom: '20px' }}>
        <h3>Add New Item</h3>
        <form onSubmit={handleAddItem}>
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
          <button type="submit" className="btn" style={{ width: 'auto' }}>
            + Add Item
          </button>
        </form>
      </div>

      <div className="card">
        <h3>Your Items</h3>
        {loading ? (
          <div>Loading items...</div>
        ) : (
          <ul>
            {items.map((item) => (
              <li 
                key={item.id} 
                className="list-item" 
                style={{ 
                  display: 'flex', 
                  justifyContent: 'space-between', 
                  alignItems: 'center',
                  textDecoration: item.completed ? 'line-through' : 'none',
                  opacity: item.completed ? 0.6 : 1
                }}
              >
                <div>
                  <strong>{item.description}</strong>
                </div>
                <div>
                  <button 
                    className="btn" 
                    style={{ width: 'auto', background: '#28a745', marginRight: '5px' }}
                    onClick={() => handleToggleItem(item.id)}
                  >
                    {item.completed ? 'Undo' : '✓ Done'}
                  </button>
                  <button 
                    className="btn btn-logout" 
                    style={{ width: 'auto' }}
                    onClick={() => handleDeleteItem(item.id)}
                  >
                    Delete
                  </button>
                </div>
              </li>
            ))}
            {items.length === 0 && !loading && (
              <li className="list-item">Your checklist is empty.</li>
            )}
          </ul>
        )}
      </div>
    </div>
  );
};

export default ChecklistPage;