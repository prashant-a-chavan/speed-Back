import React from 'react';
import { Routes, Route } from 'react-router-dom'; // Import routing components
import './App.css';

import { useSpeedback } from './hooks/useSpeedback.ts';
import { Modal } from './components/Modal.tsx';
import { Navbar } from './components/Navbar.tsx';
import { DashboardPage } from './pages/DashboardPage.tsx';
import { AboutPage } from './pages/AboutPage.tsx';

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
      <Navbar />
      <Routes>
        <Route
          path="/"
          element={
            <DashboardPage
              teamMembers={teamMembers}
              bookings={bookings}
              selectedBooker={selectedBooker}
              setSelectedBooker={setSelectedBooker}
              handleBooking={handleBooking}
              handleRemoveBooking={handleRemoveBooking}
              getAvailableBookies={getAvailableBookies}
            />
          }
        />
        <Route path="/about" element={<AboutPage />} />
      </Routes>

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
