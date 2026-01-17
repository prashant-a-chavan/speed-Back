export interface FeatureFlag {
  name: string;
  isActive: boolean;
}

export interface FeatureFlagsConfig {
  flags: FeatureFlag[];
}

export const ENV_CONFIG: FeatureFlagsConfig = {
  flags: [
    {
      name: 'REMOVE_BOOKINGS',
      isActive: true,
    },
  ],
};

export const isFeatureEnabled = (featureName: string): boolean => {
  const flag = ENV_CONFIG.flags.find((f) => f.name === featureName);
  return flag ? flag.isActive : false;
};
