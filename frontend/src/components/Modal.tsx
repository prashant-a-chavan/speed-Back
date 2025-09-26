import React from 'react';
import './Modal.css';

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  children: React.ReactNode;
  type?: 'error' | 'success' | 'info';
}

export const Modal: React.FC<ModalProps> = ({ isOpen, onClose, title, children, type }) => {
  if (!isOpen) {
    return null;
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2 className="modal-title">{title}</h2>
          <button className="modal-close-button" onClick={onClose} title="Close">
            &times;
          </button>
        </div>
        <div className="modal-body">
          {type && <div className={`modal-icon ${type}`}></div>}
          <div>{children}</div>
        </div>
        <div className="modal-footer">
          <button className="modal-ok-button" onClick={onClose}>
            OK
          </button>
        </div>
      </div>
    </div>
  );
};
