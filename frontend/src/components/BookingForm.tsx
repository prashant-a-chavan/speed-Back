import React, { useState } from 'react';
import './BookingForm.css';
import { TeamMember } from '../types';

interface BookingFormProps {
  teamMembers: TeamMember[];
  selectedBooker: number | null;
  setSelectedBooker: (id: number | null) => void;
  handleBooking: (booking: { bookerId: number; bookieId: number; slotNumber: number }) => void;
  getAvailableBookies: (bookerId: number, slotNumber: number) => TeamMember[];
}

export const BookingForm: React.FC<BookingFormProps> = ({
  teamMembers,
  selectedBooker,
  setSelectedBooker,
  handleBooking,
  getAvailableBookies,
}) => {
  const [selectedBookie, setSelectedBookie] = useState<number | ''>('');
  const [selectedSlot, setSelectedSlot] = useState<number>(1);

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (selectedBooker === null || selectedBookie === '') {
      alert('Please select yourself and a team member to book.');
      return;
    }

    handleBooking({
      bookerId: selectedBooker,
      bookieId: Number(selectedBookie),
      slotNumber: selectedSlot,
    });

    setSelectedBookie('');
  };

  return (
    <div className="booking-form">
      <h2>Make a Booking</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Your Name
          <select
            value={selectedBooker ?? ''}
            onChange={(e) => setSelectedBooker(e.target.value ? Number(e.target.value) : null)}
            required
          >
            <option value="">Select Yourself</option>
            {teamMembers.map((member) => (
              <option key={member.id} value={member.id}>
                {member.name}
              </option>
            ))}
          </select>
        </label>

        <label>
          Slot
          <select value={selectedSlot} onChange={(e) => setSelectedSlot(Number(e.target.value))}>
            {[1, 2, 3].map((slotNum) => (
              <option key={slotNum} value={slotNum}>
                Slot {slotNum} (15 mins)
              </option>
            ))}
          </select>
        </label>

        <label>
          Book Feedback With
          <select
            value={selectedBookie}
            onChange={(e) => setSelectedBookie(e.target.value ? Number(e.target.value) : '')}
            required
            disabled={!selectedBooker}
          >
            <option value="">Select a Team Member</option>
            {selectedBooker &&
              getAvailableBookies(selectedBooker, selectedSlot).map((member) => (
                <option key={member.id} value={member.id}>
                  {member.name}
                </option>
              ))}
          </select>
        </label>

        <button type="submit">Book Slot</button>
      </form>
    </div>
  );
};
