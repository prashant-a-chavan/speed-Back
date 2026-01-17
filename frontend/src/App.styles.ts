import styled from '@emotion/styled';

export const AppContainer = styled('div')(() => ({
  display: 'flex' as const,
  flexDirection: 'column' as const,
  height: '100vh',
  background: 'var(--background-gradient)' as const,
}));
