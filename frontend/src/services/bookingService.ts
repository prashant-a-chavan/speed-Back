import { AxiosResponse } from 'axios';
import apiClient from '../api/apiClient';
import { Booking, TeamMember } from '../types';

export const getTeamMembers = (): Promise<AxiosResponse<TeamMember[]>> =>
  apiClient.get('/team-members');

export const getBookings = (): Promise<AxiosResponse<Booking[]>> => apiClient.get('/bookings');

export const createBooking = (
  booking: Omit<Booking, 'id' | 'bookerName' | 'bookieName'>
): Promise<AxiosResponse<Booking>> => apiClient.post('/bookings', booking);

export const removeBooking = (bookerId: number, slotNumber: number): Promise<AxiosResponse<void>> =>
  apiClient.delete(`/bookings/${bookerId}/${slotNumber}`);
