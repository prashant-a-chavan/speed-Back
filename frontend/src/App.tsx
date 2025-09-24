import React, { useState, useEffect, useCallback } from "react";
import "./App.css";

import { TeamMember, Booking } from "./types";

import { Modal } from "./components/Modal.tsx";
import { BookingForm } from "./components/BookingForm.tsx";
import { Dashboard } from "./components/Dashboard.tsx";

import { getTeamMembers, getBookings, createBooking, removeBooking } from "./services/bookingService";

import useWebSocket from "./hooks/useWebSocket.js";

const WS_URL = "http://localhost:8080/ws";

export const App: React.FC = () => {
  const [teamMembers, setTeamMembers] = useState<TeamMember[]>([]);
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [selectedBooker, setSelectedBooker] = useState<number | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMessage, setModalMessage] = useState("");

  const handleWebSocketMessage = useCallback((updatedBookings: Booking[]) => {
    setBookings(updatedBookings);
  }, []);

  useWebSocket(WS_URL, handleWebSocketMessage);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [membersResponse, bookingsResponse] = await Promise.all([
          getTeamMembers(),
          getBookings(),
        ]);
        setTeamMembers(membersResponse.data);
        setBookings(bookingsResponse.data);
      } catch (error) {
        console.error("Error fetching initial data:", error);
        alert("Failed to load initial data from the server.");
      }
    };
    fetchData();
  }, []);

  const handleBooking = async (bookingData: Omit<Booking, "id" | "bookerName" | "bookieName">) => {
    try {
      await createBooking(bookingData);
    } catch (error: any) {
      const errorMessage = error.response?.data || error.message;
      console.error("Error creating booking:", errorMessage);
      setModalMessage(errorMessage);
      setIsModalOpen(true);
    }
  };

  const handleRemoveBooking = async (bookerId: number, slotNumber: number) => {
    try {
      await removeBooking(bookerId, slotNumber);
    } catch (error: any) {
      const errorMessage = error.response?.data || error.message;
      console.error("Error removing booking:", errorMessage);
      setModalMessage(errorMessage);
      setIsModalOpen(true);    }
  };

  const getAvailableBookies = useCallback((bookerId: number, currentSlot: number): TeamMember[] => {
      const bookedByBooker = bookings
        .filter((b) => b.bookerId === bookerId)
        .map((b) => b.bookieId);
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
    }, [bookings, teamMembers]
  );

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
        title="Booking Failed"
        type="error"
      >
           <p>{modalMessage}</p>
      </Modal>
    </div>
  );
};