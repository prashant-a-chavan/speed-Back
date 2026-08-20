import React, { createContext, useContext } from 'react';

const FeatureFlagsContext = createContext<Record<string, boolean>>({});

interface FeatureFlagsProviderProps {
  flags: Record<string, boolean>;
  children: React.ReactNode;
}

export const FeatureFlagsProvider: React.FC<FeatureFlagsProviderProps> = ({ flags, children }) => (
  <FeatureFlagsContext.Provider value={flags}>{children}</FeatureFlagsContext.Provider>
);

export const useFeatureFlag = (flagName: string): boolean => {
  const flags = useContext(FeatureFlagsContext);
  return flags[flagName] ?? false;
};
