import React, { useState, useEffect, useCallback } from 'react';
import {
  getTeamMembers,
  getBookings,
  createBooking,
  removeBooking,
} from '../services/bookingService';
import { getSlotConfiguration } from '../services/configService';
import useWebSocket from './useWebSocket';
import { TeamMember, Booking, SlotConfig } from '../types';

const WS_URL = process.env.REACT_APP_WS_URL || 'http://localhost:8080/ws';

interface UseSpeedbackReturn {
  teamMembers: TeamMember[];
  bookings: Booking[];
  selectedBooker: number | null;
  setSelectedBooker: React.Dispatch<React.SetStateAction<number | null>>;
  isModalOpen: boolean;
  setIsModalOpen: React.Dispatch<React.SetStateAction<boolean>>;
  modalMessage: string;
  handleBooking: (bookingData: Omit<Booking, 'id' | 'bookerName' | 'bookieName'>) => Promise<void>;
  handleRemoveBooking: (bookerId: number, slotNumber: number) => Promise<void>;
  getAvailableBookies: (bookerId: number, currentSlot: number) => TeamMember[];
  slotConfig: SlotConfig;
}

export const useSpeedback = (): UseSpeedbackReturn => {
  const [teamMembers, setTeamMembers] = useState<TeamMember[]>([]);
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [slotConfig, setSlotConfig] = useState<SlotConfig>({ count: 3, durationMinutes: 15 });

  const [selectedBooker, setSelectedBooker] = useState<number | null>(null);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMessage, setModalMessage] = useState('');

  const handleWebSocketMessage = useCallback((updatedBookings: Booking[]) => {
    setBookings(updatedBookings);
  }, []);

  useWebSocket(WS_URL, handleWebSocketMessage);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [membersResponse, bookingsResponse, configResponse] = await Promise.all([
          getTeamMembers(),
          getBookings(),
          getSlotConfiguration(),
        ]);
        setTeamMembers(membersResponse.data);
        setBookings(bookingsResponse.data);
        setSlotConfig(configResponse);
      } catch (error) {
        console.error('Error fetching initial data:', error);
        setModalMessage('Failed to load initial data from the server.');
        setIsModalOpen(true);
      }
    };
    fetchData();
  }, []);

  const handleBooking = async (bookingData: Omit<Booking, 'id' | 'bookerName' | 'bookieName'>) => {
    const bookerAlreadyBooked = bookings.find(
      (b) => b.bookieId === bookingData.bookerId && b.slotNumber === bookingData.slotNumber
    );

    if (bookerAlreadyBooked) {
      const bookerWho = teamMembers.find((m) => m.id === bookerAlreadyBooked.bookerId);
      const bookerWhoName = bookerWho ? bookerWho.name : 'another user';
      setModalMessage(
        `You are already booked for slot ${bookingData.slotNumber} by ${bookerWhoName}.`
      );
      setIsModalOpen(true);
      return;
    }

    try {
      await createBooking(bookingData);

      const bookieName =
        teamMembers.find((m) => m.id === bookingData.bookieId)?.name || 'team member';
      setModalMessage(`Successfully booked ${bookieName} for slot ${bookingData.slotNumber}!`);
      setIsModalOpen(true);
    } catch (error: any) {
      const errorMessage = error.response?.data || 'An unexpected error occurred.';
      console.error('Error creating booking:', errorMessage);
      setModalMessage(errorMessage);
      setIsModalOpen(true);
    }
  };

  const handleRemoveBooking = async (bookerId: number, slotNumber: number) => {
    try {
      await removeBooking(bookerId, slotNumber);
    } catch (error: any) {
      const errorMessage = error.response?.data || 'Could not remove the booking.';
      console.error('Error removing booking:', errorMessage);
      setModalMessage(errorMessage);
      setIsModalOpen(true);
    }
  };

  const getAvailableBookies = useCallback(
    (bookerId: number, currentSlot: number): TeamMember[] => {
      if (!bookerId) return [];
      const bookedByBooker = bookings.filter((b) => b.bookerId === bookerId).map((b) => b.bookieId);
      const bookedBookiesInSlot = bookings
        .filter((b) => b.slotNumber === currentSlot)
        .map((b) => b.bookieId);
      const bookedBookersInSlot = bookings
        .filter((b) => b.slotNumber === currentSlot)
        .map((b) => b.bookerId);
      return teamMembers.filter(
        (member) =>
          member.id !== bookerId &&
          !bookedByBooker.includes(member.id) &&
          !bookedBookiesInSlot.includes(member.id) &&
          !bookedBookersInSlot.includes(member.id)
      );
    },
    [bookings, teamMembers]
  );

  return {
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
  };
};
