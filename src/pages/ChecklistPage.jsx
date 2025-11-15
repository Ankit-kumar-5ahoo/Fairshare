import React, { useState, useEffect } from 'react';
// import api from '../api';

// --- MOCK DATA ---
const mockChecklist = [
  { id: 1, title: "Pay rent", note: "Before 5th", isCompleted: false },
  { id: 2, title: "Buy groceries", note: "Milk, eggs, bread", isCompleted: true },
  { id: 3, title: "Book flight tickets", note: "For Goa trip", isCompleted: false },
];
// -----------------

const ChecklistPage = () => {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  // State for the new item form
  const [title, setTitle] = useState("");
  const [note, setNote] = useState("");

  useEffect(() => {
    // --- REAL API CALL (for later) ---
    // const fetchItems = async () => {
    //   const res = await api.get('/checklist/my');
    //   setItems(res.data);
    //   setLoading(false);
    // };
    // fetchItems();

    // --- MOCK LOGIC (for now) ---
    setTimeout(() => {
      setItems(mockChecklist);
      setLoading(false);
    }, 500);
  }, []);

  const handleAddItem = (e) => {
    e.preventDefault();
    // --- REAL API CALL (for later) ---
    // await api.post('/checklist', { title, note });
    // (Then refetch items)

    // --- MOCK LOGIC (for now) ---
    console.log("Adding item:", { title, note });
    alert("Mock: Item added!");
    setTitle("");
    setNote("");
  };

  const handleToggleItem = (id) => {
    // --- REAL API CALL (for later) ---
    // await api.put(`/checklist/${id}/toggle`);
    // (Then refetch items)

    // --- MOCK LOGIC (for now) ---
    console.log("Toggling item:", id);
    // This just fakes the toggle on the frontend
    setItems(prevItems =>
      prevItems.map(item =>
        item.id === id ? { ...item, isCompleted: !item.isCompleted } : item
      )
    );
  };

  const handleDeleteItem = (id) => {
    if (window.confirm("Delete this item?")) {
      // --- REAL API CALL (for later) ---
      // await api.delete(`/checklist/${id}`);
      // (Then refetch items)

      // --- MOCK LOGIC (for now) ---
      console.log("Deleting item:", id);
      alert("Mock: Item deleted!");
    }
  };

  return (
    <div className="container">
      <h2>My Personal Checklist</h2>
      <p>This list is private and not shared with any group.</p>

      {/* --- Add Item Form --- */}
      <div className="card" style={{ marginBottom: '20px' }}>
        <h3>Add New Item</h3>
        <form onSubmit={handleAddItem}>
          <div className="form-group">
            <label htmlFor="title">Title:</label>
            <input
              type="text"
              id="title"
              className="form-input"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="note">Note (Optional):</label>
            <input
              type="text"
              id="note"
              className="form-input"
              value={note}
              onChange={(e) => setNote(e.target.value)}
            />
          </div>
          <button type="submit" className="btn" style={{ width: 'auto' }}>
            + Add Item
          </button>
        </form>
      </div>

      {/* --- Checklist --- */}
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
                  // Apply a style if completed
                  textDecoration: item.isCompleted ? 'line-through' : 'none',
                  opacity: item.isCompleted ? 0.6 : 1
                }}
              >
                <div>
                  <strong>{item.title}</strong>
                  <br />
                  <small>{item.note}</small>
                </div>
                <div>
                  <button 
                    className="btn" 
                    style={{ width: 'auto', background: '#28a745', marginRight: '5px' }}
                    onClick={() => handleToggleItem(item.id)}
                  >
                    {item.isCompleted ? 'Undo' : '✓ Done'}
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
          </ul>
        )}
      </div>
    </div>
  );
};

export default ChecklistPage;