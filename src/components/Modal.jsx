import React from 'react';

// This is the CSS for the modal. We'll add it to App.css.
// It creates a dark overlay and a centered white box.

const Modal = ({ children, onClose }) => {
  return (
    // 1. The dark background overlay
    // onClick is set so if you click *outside* the box, it closes
    <div className="modal-overlay" onClick={onClose}>

      {/* 2. The white content box */}
      {/* onClick with e.stopPropagation() prevents the modal from
          closing when you click *inside* the box */}
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>

        {/* 3. A close button */}
        <button className="modal-close-btn" onClick={onClose}>
          &times; {/* This is just an "X" icon */}
        </button>

        {/* 4. The actual content (e.g., our form) goes here */}
        {children}
      </div>
    </div>
  );
};

export default Modal;