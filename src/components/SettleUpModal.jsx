import React, { useState, useEffect } from 'react';
import Modal from './Modal';
// import api from '../api'; // For later

// --- MOCK DATA (matches API docs) ---
// GET /api/balance/{groupId}/simplify
const mockSettlements = [
  "Rahul pays 175 to Ankit",
  "Sana pays 90 to Rahul"
];
// ------------------------------------

const SettleUpModal = ({ onClose, groupId }) => {
  const [settlements, setSettlements] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // --- REAL API CALL (for later) ---
    // const fetchSettlements = async () => {
    //   try {
    //     const res = await api.get(`/balance/${groupId}/simplify`);
    //     setSettlements(res.data);
    //   } catch (err) {
    //     console.error("Failed to fetch settlements", err);
    //   } finally {
    //     setLoading(false);
    //   }
    // };
    // fetchSettlements();

    // --- MOCK DATA CALL (for now) ---
    setTimeout(() => {
      setSettlements(mockSettlements);
      setLoading(false);
    }, 500); // 500ms delay
  }, [groupId]);

  return (
    <Modal onClose={onClose}>
      <h2>Settle Up</h2>
      <p>Here are the simplified settlements for your group:</p>

      {loading ? (
        <div>Loading settlements...</div>
      ) : (
        <ul style={{ listStyle: 'none', paddingLeft: '0', marginTop: '20px' }}>
          {settlements.map((item, index) => (
            <li 
              key={index} 
              className="list-item" 
              style={{ background: '#f9f9f9', textAlign: 'center', fontWeight: '500' }}
            >
              {item}
            </li>
          ))}
        </ul>
      )}

      <button 
        className="btn" 
        onClick={onClose} 
        style={{ width: 'auto', marginTop: '20px', background: '#6c757d' }}
      >
        Close
      </button>
    </Modal>
  );
};

export default SettleUpModal;