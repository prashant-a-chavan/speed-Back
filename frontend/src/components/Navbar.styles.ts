import styled from '@emotion/styled';
import { NavLink } from 'react-router-dom';

export const NavbarContainer = styled('nav')(() => ({
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
  padding: '0 1.5rem',
  height: '4.5rem',
  background: 'rgba(255, 255, 255, 0.7)',
  backdropFilter: 'blur(10px)',
  borderBottom: '1px solid var(--border-primary)',
  flexShrink: 0,
}));

export const StyledNavLink = styled(NavLink)(() => ({
  textDecoration: 'none',
  color: 'var(--text-secondary)',
  fontWeight: 600,
  padding: '0.5rem 1rem',
  borderRadius: 'var(--radius-md)',
  transition: 'var(--transition-fast)',

  '&:hover': {
    backgroundColor: 'var(--surface-secondary)',
    color: 'var(--text-primary)',
  },

  '&.active': {
    background: 'var(--accent-gradient)',
    color: 'rgb(234, 234, 234)',
    fontWeight: 700,
    transition: 'color 0.6s, fontWeight 0.6s',
  },
}));
