import React from 'react';
import { NavbarContainer, StyledNavLink } from './Navbar.styles.ts';
import { Typography } from '@mui/material';

export const Navbar: React.FC = () => {
  return (
    <NavbarContainer>
      <Typography variant={'h5'} fontWeight={'600'}>
        Speed Back Dashboard
      </Typography>
      <Typography sx={{ display: 'flex', gap: 2 }}>
        <StyledNavLink to="/">Dashboard</StyledNavLink>
        <StyledNavLink to="/about">About</StyledNavLink>
      </Typography>
    </NavbarContainer>
  );
};
