import styled from '@emotion/styled';
import { Select, Box, InputLabel } from '@mui/material';

export const FormContainer = styled(Box)(() => ({
  width: '340px',
  flexShrink: 0,
  display: 'flex',
  flexDirection: 'column' as const,
  padding: '32px 24px',
  background: 'radial-gradient(ellipse at bottom, #1b2735 0%, #090a0f 100%)',
  color: 'var(--text-on-dark)',
  borderRadius: 'var(--radius-lg)',
  boxShadow: 'var(--shadow-lg)',
  overflowY: 'auto' as const,

  '@media (max-width: 1024px)': {
    width: '100%',
  },
}));

export const Button = styled('button')(() => ({
  fontSize: '15px',
  marginTop: '1rem',
  padding: '0.7em 2.7em',
  letterSpacing: '0.06em',
  position: 'relative' as const,
  fontFamily: 'inherit',
  borderRadius: '0.6em',
  overflow: 'hidden' as const,
  transition: 'all 0.3s' as const,
  lineHeight: '1.4em' as const,
  border: '2px solid #1BFD9C' as const,
  background:
    'linear-gradient(to right, rgba(27, 253, 156, 0.1) 1%, transparent 40%,transparent 60% , rgba(27, 253, 156, 0.1) 100%)' as const,
  color: '#1BFD9C' as const,
  boxShadow: 'inset 0 0 10px rgba(27, 253, 156, 0.4), 0 0 9px 3px rgba(27, 253, 156, 0.1)' as const,

  '&:disabled': {
    opacity: 0.5,
    cursor: 'not-allowed',
    border: 0,
  },

  '&:hover:not(:disabled)': {
    color: '#82ffc9',
    boxShadow:
      'inset 0 0 10px rgba(27, 253, 156, 0.6), 0 0 9px 3px rgba(27, 253, 156, 0.2)' as const,
  },

  '&:before': {
    content: '""' as const,
    position: 'absolute' as const,
    left: '-4em' as const,
    width: '4em' as const,
    height: '100%' as const,
    top: 0 as const,
  },
}));

export const StyledSelect = styled(Select)(() => ({
  fontFamily: 'inherit',
  fontSize: '0.95rem',
  borderRadius: 'var(--radius-md)',
  backgroundColor: '#34495e',
  color: 'var(--text-on-dark)',

  '& .MuiSelect-select': {
    padding: '12px 14px',
  },

  '& .MuiOutlinedInput-notchedOutline': {
    borderColor: '#4a6278',
  },

  '&:hover .MuiOutlinedInput-notchedOutline': {
    borderColor: '#4a6278',
  },

  '& .Mui-focused .MuiOutlinedInput-notchedOutline': {
    borderColor: 'var(--accent-color-dark)',
    boxShadow: '0 0 0 3px rgba(91, 134, 229, 0.3)',
  },

  '& .Mui-disabled': {
    backgroundColor: '#2c3e50',
    opacity: 0.6,
    cursor: 'not-allowed',
  },
}));

export const StyledInputLabel = styled(InputLabel)(() => ({
  color: '#aeb8c4',
  fontSize: '0.875rem',
  fontWeight: 500,
  marginBottom: '8px',

  '& .Mui-focused': {
    color: '#5b86e5',
  },

  '& .Mui-disabled': {
    color: '#6c7a89',
    opacity: 0.7,
  },

  '& .Mui-error': {
    color: '#e74c3c',
  },
}));
