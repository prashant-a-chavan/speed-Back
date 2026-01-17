import apiClient from '../api/apiClient';
import { SlotConfig } from '../types';

export const getSlotConfiguration = async (): Promise<SlotConfig> => {
  const response = await apiClient.get('/config/slots');
  return response.data;
};
