import React, { useState, useCallback, useMemo } from 'react';
import { TeamMember, SlotConfig } from '../types';
import { Button, FormContainer, StyledSelect, StyledInputLabel } from './BookingForm.styles';
import { MenuItem, Typography, FormControl, Box } from '@mui/material';

interface BookingFormProps {
  teamMembers: TeamMember[];
  selectedBooker: number | null;
  setSelectedBooker: (id: number | null) => void;
  handleBooking: (booking: { bookerId: number; bookieId: number; slotNumber: number }) => void;
  getAvailableBookies: (bookerId: number, slotNumber: number) => TeamMember[];
  slotConfig: SlotConfig;
}

export const BookingForm: React.FC<BookingFormProps> = ({
  teamMembers,
  selectedBooker,
  setSelectedBooker,
  handleBooking,
  getAvailableBookies,
  slotConfig,
}) => {
  const slots = useMemo(() => {
    return Array.from({ length: slotConfig.count }, (_, i) => i + 1);
  }, [slotConfig.count]);

  const [selectedBookie, setSelectedBookie] = useState<number | null>(null);
  const [selectedSlot, setSelectedSlot] = useState<number>(1);

  const resetForm = useCallback(() => {
    setSelectedBookie(null);
  }, []);

  const handleSubmit = useCallback(
    (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();

      if (!selectedBooker) {
        alert('Please select yourself.');
        return;
      }

      if (!selectedBookie) {
        alert('Please select a team member to book with.');
        return;
      }

      handleBooking({
        bookerId: selectedBooker,
        bookieId: selectedBookie,
        slotNumber: selectedSlot,
      });

      resetForm();
    },
    [selectedBooker, selectedBookie, selectedSlot, handleBooking, resetForm]
  );

  const availableBookies = useMemo(() => {
    if (!selectedBooker) return [];
    return getAvailableBookies(selectedBooker, selectedSlot);
  }, [selectedBooker, selectedSlot, getAvailableBookies]);

  return (
    <FormContainer>
      <Typography variant="h5" fontWeight={700} color="#ffffff" mb={2}>
        Make a Booking
      </Typography>

      <Box component="form" display="flex" flexDirection="column" onSubmit={handleSubmit}>
        <Box mb={3}>
          <FormControl fullWidth required>
            <StyledInputLabel id="booker-label">Select Yourself</StyledInputLabel>
            <StyledSelect
              id="booker-select"
              labelId="booker-label"
              label="Select Yourself"
              variant="outlined"
              value={selectedBooker ?? ''}
              onChange={(e) => {
                const value = e.target.value;
                setSelectedBooker(value === '' ? null : Number(value));
              }}
            >
              <MenuItem value="">
                <em>Select Yourself</em>
              </MenuItem>
              {teamMembers.map((member) => (
                <MenuItem key={member.id} value={member.id}>
                  {member.name}
                </MenuItem>
              ))}
            </StyledSelect>
          </FormControl>
        </Box>

        <Box mb={3}>
          <FormControl fullWidth>
            <StyledInputLabel id="slot-label">Slot</StyledInputLabel>
            <StyledSelect
              id="slot-select"
              labelId="slot-label"
              label="Slot"
              variant="outlined"
              value={selectedSlot}
              onChange={(e) => setSelectedSlot(Number(e.target.value))}
            >
              {slots.map((slotNum) => (
                <MenuItem key={slotNum} value={slotNum}>
                  Slot {slotNum} ({slotConfig.durationMinutes} mins)
                </MenuItem>
              ))}
            </StyledSelect>
          </FormControl>
        </Box>

        <Box mb={2}>
          <FormControl fullWidth required disabled={!selectedBooker}>
            <StyledInputLabel id="bookie-label">Select a Team Member</StyledInputLabel>
            <StyledSelect
              id="bookie-select"
              labelId="bookie-label"
              label="Select a Team Member"
              variant="outlined"
              value={selectedBookie ?? ''}
              onChange={(e) => {
                const value = e.target.value;
                setSelectedBookie(value === '' ? null : Number(value));
              }}
            >
              <MenuItem value="">
                <em>Select a Team Member</em>
              </MenuItem>

              {availableBookies.map((member) => (
                <MenuItem key={member.id} value={member.id}>
                  {member.name}
                </MenuItem>
              ))}
            </StyledSelect>
          </FormControl>
        </Box>

        <Button type="submit" disabled={!selectedBooker || !selectedBookie}>
          Book Slot
        </Button>
      </Box>
    </FormContainer>
  );
};
