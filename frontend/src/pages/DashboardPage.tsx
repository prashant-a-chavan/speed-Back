import React from 'react';
import { BookingForm } from '../components/BookingForm';
import { Dashboard } from '../components/Dashboard';
import { TeamMember, Booking, SlotConfig } from '../types';
import { Box } from '@mui/material';

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
    <Box
      display="flex"
      flexGrow={1}
      overflow="hidden"
      p={3}
      gap={3}
      sx={{ '@media (max-width: 1024px)': { flexDirection: 'column', p: 2, gap: 2 } }}
    >
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
    </Box>
  );
};
