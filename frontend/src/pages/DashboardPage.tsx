import React from 'react';
import { BookingForm } from '../components/BookingForm';
import { Dashboard } from '../components/Dashboard';
import { TeamMember, Booking, SlotConfig } from '../types';
import { Typography } from '@mui/material';
import styled from '@emotion/styled';

const MainLayout = styled(Typography)`
  display: flex;
  flex-grow: 1;
  overflow: hidden;
  padding: 24px;
  gap: 24px;

  @media (max-width: 1024px) {
    flex-direction: column;
    padding: 16px;
    gap: 16px;
  }
`;

interface DashboardPageProps {
  teamMembers: TeamMember[];
  bookings: Booking[];
  selectedBooker: number | null;
  setSelectedBooker: (id: number | null) => void;
  handleBooking: (bookingData: Omit<Booking, 'id' | 'bookerName' | 'bookieName'>) => Promise<void>;
  handleRemoveBooking: (bookerId: number, slotNumber: number) => Promise<void>;
  getAvailableBookies: (bookerId: number, currentSlot: number) => TeamMember[];
  slotConfig: SlotConfig;
}

export const DashboardPage: React.FC<DashboardPageProps> = ({
  teamMembers,
  bookings,
  selectedBooker,
  setSelectedBooker,
  handleBooking,
  handleRemoveBooking,
  getAvailableBookies,
  slotConfig,
}) => {
  return (
    <MainLayout>
      <BookingForm
        teamMembers={teamMembers}
        selectedBooker={selectedBooker}
        setSelectedBooker={setSelectedBooker}
        handleBooking={handleBooking}
        getAvailableBookies={getAvailableBookies}
        slotConfig={slotConfig}
      />
      <Dashboard
        teamMembers={teamMembers}
        bookings={bookings}
        currentBooker={selectedBooker}
        onRemove={handleRemoveBooking}
        slotConfig={slotConfig}
      />
    </MainLayout>
  );
};
