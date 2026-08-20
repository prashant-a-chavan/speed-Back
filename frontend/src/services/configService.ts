import apiClient from '../api/apiClient';
import { SlotConfig, FeatureFlag } from '../types';

export const getSlotConfiguration = async (): Promise<SlotConfig> => {
  const response = await apiClient.get('/config/slots');
  return response.data;
};

export const getFeatureFlags = async (): Promise<Record<string, boolean>> => {
  const response = await apiClient.get<FeatureFlag[]>('/feature-flags');
  return Object.fromEntries(response.data.map((f) => [f.name, f.active]));
};
