import React from 'react';
import { Routes, Route } from 'react-router-dom';

import { useSpeedback } from './hooks/useSpeedback.ts';
import { Modal } from './components/Modal.tsx';
import { Navbar } from './components/Navbar.tsx';
import { DashboardPage } from './pages/DashboardPage.tsx';
import { AboutPage } from './pages/AboutPage.tsx';
import { AppContainer } from './App.styles.ts';

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
    slotConfig,
  } = useSpeedback();

  const getModalType = (message: string): 'error' | 'success' | 'info' => {
    if (message.includes('Failed') || message.includes('not available')) {
      return 'error';
    }
    if (message.includes('Successfully')) {
      return 'success';
    }
    return 'info';
  };

  return (
    <AppContainer>
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
              slotConfig={slotConfig}
            />
          }
        />
        <Route path="/about" element={<AboutPage />} />
      </Routes>

      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Booking Information"
        type={getModalType(modalMessage)}
      >
        <p>{modalMessage}</p>
      </Modal>
    </AppContainer>
  );
};
