export interface TeamMember {
  id: number;
  name: string;
}

export interface Booking {
  id: number;
  bookerId: number;
  bookieId: number;
  slotNumber: number;
  bookerName: string;
  bookieName: string;
}

export interface SlotConfig {
  count: number;
  durationMinutes: number;
}

export interface FeatureFlag {
  name: string;
  active: boolean;
}
