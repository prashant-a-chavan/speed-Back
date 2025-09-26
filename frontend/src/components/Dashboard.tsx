import React from 'react';
import { BookingRow } from './BookingRow.tsx';
import './Dashboard.css';
import { TeamMember, Booking } from '../types'; // Import from the central types file

interface DashboardProps {
  teamMembers: TeamMember[];
  bookings: Booking[];
  currentBooker: number | null;
  onRemove: (bookerId: number, slotNumber: number) => void;
}

export const Dashboard: React.FC<DashboardProps> = ({
  teamMembers,
  bookings,
  currentBooker,
  onRemove,
}) => {
  return (
    <div className="dashboard">
      <h2>Current Bookings</h2>
      <table>
        <thead>
          <tr>
            <th>Team Member</th>
            <th>Slot 1</th>
            <th>Slot 2</th>
            <th>Slot 3</th>
          </tr>
        </thead>
        <tbody>
          {teamMembers.map((member) => (
            <BookingRow
              key={member.id}
              member={member}
              bookings={bookings}
              currentBooker={currentBooker}
              onRemove={onRemove}
            />
          ))}
        </tbody>
      </table>
    </div>
  );
};
