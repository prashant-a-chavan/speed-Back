import React from 'react';
import { TableCell, Tooltip, IconButton, SxProps, Theme } from '@mui/material';
import ClearIcon from '@mui/icons-material/Clear';
import { Booking, TeamMember } from '../types';
import { isFeatureEnabled } from '../config/featureFlags';

interface BookingCellProps {
  variant: 'booking';
  slot: number;
  bookingAsBooker?: Booking;
  bookingAsBookie?: Booking;
  canCancel: boolean;
  onCancel: (bookerId: number, slotNumber: number) => void;
}

interface BookingMemberNameProps {
  variant: 'member';
  member: TeamMember;
}

type TableCellContentProps = BookingCellProps | BookingMemberNameProps;

export const TableCellContent: React.FC<TableCellContentProps> = (props) => {
  const renderContent = () => {
    if (props.variant === 'member') {
      return props.member.name;
    }

    const { bookingAsBooker, bookingAsBookie, canCancel, onCancel, slot } = props;

    if (bookingAsBooker) {
      const showCancelButton = canCancel && isFeatureEnabled('REMOVE_BOOKINGS');

      return (
        <>
          To: {bookingAsBooker.bookieName}
          {showCancelButton && (
            <Tooltip title={`Cancel booking with ${bookingAsBooker.bookieName}`}>
              <IconButton
                onClick={() => onCancel(bookingAsBooker.bookerId, slot)}
                size="small"
                aria-label={`Cancel booking with ${bookingAsBooker.bookieName}`}
              >
                <ClearIcon color="error" />
              </IconButton>
            </Tooltip>
          )}
        </>
      );
    }

    if (bookingAsBookie) {
      return <>From: {bookingAsBookie.bookerName}</>;
    }

    return '-';
  };

  const cellStyles: SxProps<Theme> | undefined =
    props.variant === 'member' ? { color: 'grey', fontWeight: 700 } : undefined;

  return <TableCell sx={cellStyles}>{renderContent()}</TableCell>;
};
