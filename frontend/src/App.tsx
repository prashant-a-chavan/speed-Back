import React from 'react';
import './App.css';

import { useSpeedback } from './hooks/useSpeedback.ts';
import { Modal } from './components/Modal.tsx';
import { BookingForm } from './components/BookingForm.tsx';
import { Dashboard } from './components/Dashboard.tsx';

export const App: React.FC = () => {
  const {
    teamMembers,
    bookings,
    selectedBooker,
    setSelectedBooker,
    isModalOpen,
    setIsModalOpen,
    modalMessage,
    handleBooking,
    handleRemoveBooking,
    getAvailableBookies,
  } = useSpeedback();

  return (
    <div className="App">
      <h1>Speed Back Dashboard</h1>
      <div className="main-layout">
        <BookingForm
          teamMembers={teamMembers}
          selectedBooker={selectedBooker}
          setSelectedBooker={setSelectedBooker}
          handleBooking={handleBooking}
          getAvailableBookies={getAvailableBookies}
        />
        <Dashboard
          teamMembers={teamMembers}
          bookings={bookings}
          currentBooker={selectedBooker}
          onRemove={handleRemoveBooking}
        />
      </div>
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Booking Information"
        type={
          modalMessage.includes('Failed') ||
          modalMessage.includes('not available') ||
          modalMessage.includes('already have a booking')
            ? 'error'
            : 'info'
        }
      >
        <p>{modalMessage}</p>
      </Modal>
    </div>
  );
};
