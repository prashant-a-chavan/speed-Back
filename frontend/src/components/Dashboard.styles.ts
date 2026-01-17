import styled from '@emotion/styled';
import { Box, TableHead } from '@mui/material';

export const DashboardContainer = styled(Box)(() => ({
  flexGrow: 1,
  background: 'var(--surface-primary)',
  borderRadius: 'var(--radius-lg)',
  boxShadow: 'var(--shadow-lg)',
  padding: '16px',
  overflowY: 'auto' as const,
}));

export const StyledTableHead = styled(TableHead)(() => ({
  textTransform: 'uppercase' as const,
  letterSpacing: '0.75px',
  textAlign: 'left' as const,
  borderBottom: '2px solid var(--border-primary)',

  '& .MuiTableCell-root': {
    fontWeight: 700 as const,
    color: 'grey',
  },
}));
