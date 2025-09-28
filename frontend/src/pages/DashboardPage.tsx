import React from 'react';
import { BookingForm } from '../components/BookingForm';
import { Dashboard } from '../components/Dashboard';
import { TeamMember, Booking } from '../types';

interface DashboardPageProps {
  teamMembers: TeamMember[];
  bookings: Booking[];
  selectedBooker: number | null;
  setSelectedBooker: (id: number | null) => void;
  handleBooking: (bookingData: Omit<Booking, 'id' | 'bookerName' | 'bookieName'>) => Promise<void>;
  handleRemoveBooking: (bookerId: number, slotNumber: number) => Promise<void>;
  getAvailableBookies: (bookerId: number, currentSlot: number) => TeamMember[];
}

export const DashboardPage: React.FC<DashboardPageProps> = ({
  teamMembers,
  bookings,
  selectedBooker,
  setSelectedBooker,
  handleBooking,
  handleRemoveBooking,
  getAvailableBookies,
}) => {
  return (
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
  );
};
