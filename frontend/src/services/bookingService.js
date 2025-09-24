import apiClient from '../api/apiClient';

export const getTeamMembers = () => apiClient.get('/team-members');
export const getBookings = () => apiClient.get('/bookings');
export const createBooking = (booking) => apiClient.post('/bookings', booking);
export const removeBooking = (bookerId, slotNumber) => apiClient.delete(`/bookings/${bookerId}/${slotNumber}`);
