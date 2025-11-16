import React, { useState, useEffect } from 'react';
import Modal from './Modal';
import api from '../api'; 

const SettleUpModal = ({ onClose, groupId }) => {
  const [settlements, setSettlements] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchSettlements = async () => {
      try {
        
        const res = await api.get(`/balance/simplify/${groupId}`);
        
        setSettlements(res.data);
      } catch (err) {
        console.error("Failed to fetch settlements", err);
        alert("Failed to load settlements. There might be a server error.");
      } finally {
        setLoading(false);
      }
    };
    fetchSettlements();
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
          {settlements.length === 0 && !loading && (
            <li className="list-item">Everyone is settled up!</li>
          )}
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