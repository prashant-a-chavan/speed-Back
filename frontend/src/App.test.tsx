import React from 'react';
import { render, screen } from '@testing-library/react';
import { beforeEach, describe, expect, jest, test } from '@jest/globals';
import '@testing-library/jest-dom';

const mockUseSpeedback = jest.fn();
let App: React.ComponentType;

jest.mock(
  'react-router-dom',
  () => ({
    Routes: ({ children }: { children: React.ReactNode }) => <div>{children}</div>,
    Route: ({ element }: { element: React.ReactElement }) => element,
  }),
  { virtual: true }
);

jest.mock('./hooks/useSpeedback.ts', () => ({
  useSpeedback: () => mockUseSpeedback(),
}));

jest.mock('./components/Navbar.tsx', () => ({
  Navbar: () => <div>Navbar</div>,
}));

const mockModal = jest.fn(({ children }: { children: React.ReactNode }) => <div>{children}</div>);

jest.mock('./components/Modal.tsx', () => ({
  Modal: (props: { children: React.ReactNode; type: string }) => mockModal(props),
}));

jest.mock('./pages/DashboardPage.tsx', () => ({
  DashboardPage: () => <div>Dashboard Page</div>,
}));

jest.mock('./pages/AboutPage.tsx', () => ({
  AboutPage: () => <div>About Page</div>,
}));

jest.mock('./services/configService', () => ({
  getFeatureFlags: () => Promise.resolve({}),
}));

describe('App routing', () => {
  const baseHookState = {
    teamMembers: [],
    bookings: [],
    selectedBooker: null,
    setSelectedBooker: jest.fn(),
    isModalOpen: true,
    setIsModalOpen: jest.fn(),
    handleBooking: jest.fn(),
    handleRemoveBooking: jest.fn(),
    getAvailableBookies: jest.fn(() => []),
    slotConfig: { count: 3, durationMinutes: 15 },
  };

  beforeEach(() => {
    jest.clearAllMocks();
    App = require('./App.tsx').App;
  });

  test('uses success modal type for successful messages', () => {
    mockUseSpeedback.mockReturnValue({
      ...baseHookState,
      modalMessage: 'Booking Successfully created',
    });

    render(<App />);

    expect(screen.getByText('Navbar')).toBeTruthy();
    expect(screen.getByText('Dashboard Page')).toBeTruthy();
    expect(mockModal).toHaveBeenCalledWith(expect.objectContaining({ type: 'success' }));
  });

  test('uses error modal type for failure messages', () => {
    mockUseSpeedback.mockReturnValue({
      ...baseHookState,
      modalMessage: 'Failed to create booking',
    });

    render(<App />);

    expect(screen.getByText('Navbar')).toBeTruthy();
    expect(screen.getByText('About Page')).toBeTruthy();
    expect(mockModal).toHaveBeenCalledWith(expect.objectContaining({ type: 'error' }));
  });
});
