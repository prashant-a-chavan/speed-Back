import React, { useMemo } from 'react';
import { TeamMember, Booking, SlotConfig } from '../types';
import { TableRow } from '@mui/material';
import { TableCellContent } from './BookingCell.tsx';

interface BookingRowProps {
  member: TeamMember;
  bookings: Booking[];
  currentBooker: number | null;
  onRemove: (bookerId: number, slotNumber: number) => void;
  slotConfig: SlotConfig;
}

export const BookingRow: React.FC<BookingRowProps> = ({
  member,
  bookings,
  currentBooker,
  onRemove,
  slotConfig,
}) => {
  const slots = useMemo(() => {
    return Array.from({ length: slotConfig.count }, (_, i) => i + 1);
  }, [slotConfig.count]);

  const getBookingForSlot = (slot: number) => {
    const bookingAsBooker = bookings.find((b) => b.bookerId === member.id && b.slotNumber === slot);
    const bookingAsBookie = bookings.find((b) => b.bookieId === member.id && b.slotNumber === slot);

    return { bookingAsBooker, bookingAsBookie };
  };

  const canCancelBooking = currentBooker === member.id;

  return (
    <TableRow hover>
      <TableCellContent variant="member" member={member} />

      {slots.map((slot) => {
        const { bookingAsBooker, bookingAsBookie } = getBookingForSlot(slot);

        return (
          <TableCellContent
            key={slot}
            variant="booking"
            slot={slot}
            bookingAsBooker={bookingAsBooker}
            bookingAsBookie={bookingAsBookie}
            canCancel={canCancelBooking}
            onCancel={onRemove}
          />
        );
      })}
    </TableRow>
  );
};
