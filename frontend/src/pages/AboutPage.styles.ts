import styled from '@emotion/styled';
import { Box } from '@mui/material';

export const AboutPageLayout = styled(Box)(() => ({
  padding: '24px',
  width: '100%',
  flexGrow: 1,
  overflowY: 'auto' as const,
}));

export const AboutCard = styled(Box)(() => ({
  background: 'var(--surface-primary)',
  borderRadius: 'var(--radius-lg)',
  boxShadow: 'var(--shadow-lg)',
  padding: '32px 48px',
  maxWidth: '1000px',
  margin: '0 auto',
}));

export const AboutHeader = styled('div')(() => ({
  textAlign: 'center' as const,
  marginBottom: '32px',
  paddingBottom: '16px',
  borderBottom: '1px solid var(--border-primary)',

  '& h2': {
    fontSize: '2rem',
    fontWeight: 700,
    color: 'var(--text-primary)',
    background: 'var(--accent-gradient)',
    WebkitBackgroundClip: 'text',
    WebkitTextFillColor: 'transparent',
  },
}));

export const AboutSection = styled('div')(() => ({
  marginBottom: '32px',

  '& h3': {
    fontSize: '1.25rem',
    fontWeight: 600,
    color: 'var(--text-primary)',
    marginBottom: '12px',
  },

  '& p': {
    fontSize: '1rem',
    lineHeight: 1.7,
    color: 'var(--text-secondary)',
    marginBottom: '12px',
  },
}));

export const FeatureList = styled('ul')(() => ({
  listStyle: 'none',
  paddingLeft: 0,

  '& li': {
    position: 'relative' as const,
    paddingLeft: '28px',
    marginBottom: '12px',
    color: 'var(--text-secondary)',
    lineHeight: 1.7,

    '&::before': {
      content: "'✓'",
      position: 'absolute' as const,
      left: 0,
      top: '4px',
      color: 'var(--accent-color-dark)',
      fontWeight: 'bold',
    },
  },
}));

export const TechStackContainer = styled('div')(() => ({
  display: 'flex',
  gap: '32px',
  marginTop: '16px',
}));

export const TechColumn = styled('div')(() => ({
  flex: 1,

  '& h4': {
    fontSize: '1.1rem',
    fontWeight: 600,
    marginBottom: '12px',
    borderBottom: '2px solid var(--accent-color-dark)',
    paddingBottom: '4px',
    display: 'inline-block',
  },

  '& ul': {
    listStyleType: 'disc',
    paddingLeft: '20px',
    color: 'var(--text-secondary)',
  },

  '& li': {
    marginBottom: '8px',
  },
}));
