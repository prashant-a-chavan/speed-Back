import React from 'react';
import './BookingRow.css';
import { TeamMember, Booking } from '../types';

interface BookingRowProps {
  member: TeamMember;
  bookings: Booking[];
  currentBooker: number | null;
  onRemove: (bookerId: number, slotNumber: number) => void;
}

export const BookingRow: React.FC<BookingRowProps> = ({
  member,
  bookings,
  currentBooker,
  onRemove,
}) => {
  const SLOTS = [1, 2, 3];

  return (
    <tr>
      <td>{member.name}</td>
      {SLOTS.map((slot) => {
        const bookingAsBooker = bookings.find(
          (b) => b.bookerId === member.id && b.slotNumber === slot
        );
        const bookingAsBookie = bookings.find(
          (b) => b.bookieId === member.id && b.slotNumber === slot
        );

        return (
          <td key={slot}>
            {bookingAsBooker ? (
              <>
                To: <strong>{bookingAsBooker.bookieName}</strong>
                {currentBooker === member.id && (
                  <button
                    onClick={() => onRemove(bookingAsBooker.bookerId, slot)}
                    className="cancel-button"
                    title={`Cancel booking with ${bookingAsBooker.bookieName}`}
                  >
                    X
                  </button>
                )}
              </>
            ) : bookingAsBookie ? (
              <>
                From: <strong>{bookingAsBookie.bookerName}</strong>
              </>
            ) : (
              '-'
            )}
          </td>
        );
      })}
    </tr>
  );
};
