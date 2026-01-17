import React, { useMemo } from 'react';
import { BookingRow } from './BookingRow.tsx';
import { TeamMember, Booking, SlotConfig } from '../types';
import { DashboardContainer, StyledTableHead } from './Dashboard.styles.ts';
import { Table, TableBody, TableCell, TableRow, Typography } from '@mui/material';

interface DashboardProps {
  teamMembers: TeamMember[];
  bookings: Booking[];
  currentBooker: number | null;
  onRemove: (bookerId: number, slotNumber: number) => void;
  slotConfig: SlotConfig;
}

export const Dashboard: React.FC<DashboardProps> = ({
  teamMembers,
  bookings,
  currentBooker,
  onRemove,
  slotConfig,
}) => {
  const slots = useMemo(() => {
    return Array.from({ length: slotConfig.count }, (_, i) => i + 1);
  }, [slotConfig.count]);

  return (
    <DashboardContainer>
      <Typography variant="h5" fontWeight={600} mb={3}>
        Current Bookings
      </Typography>
      <Table>
        <StyledTableHead>
          <TableRow>
            <TableCell>Team Member</TableCell>
            {slots.map((slot) => (
              <TableCell key={slot}>Slot {slot}</TableCell>
            ))}
          </TableRow>
        </StyledTableHead>
        <TableBody>
          {teamMembers.map((member) => (
            <BookingRow
              key={member.id}
              member={member}
              bookings={bookings}
              currentBooker={currentBooker}
              onRemove={onRemove}
              slotConfig={slotConfig}
            />
          ))}
        </TableBody>
      </Table>
    </DashboardContainer>
  );
};
