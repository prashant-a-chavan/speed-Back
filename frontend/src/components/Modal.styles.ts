import styled from '@emotion/styled';
import { keyframes } from '@emotion/react';
import { Typography } from '@mui/material';

const fadeIn = keyframes`
    from {
      opacity: 0;
    }

    to {
      opacity: 1;
    }
`;

const slideIn = keyframes`
    from {
      transform: translateY(-30px);
      opacity: 0;
    }

    to {
      transform: translateY(0);
      opacity: 1;
    }
`;

export const ModalOverlay = styled(Typography)`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(5px);
  animation: ${fadeIn} 0.3s ease-out;
`;

export const ModalContent = styled(Typography)`
  background: var(--surface-primary);
  padding: 24px;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  width: 90%;
  max-width: 450px;
  animation: ${slideIn} 0.3s ease-out;
  border-top: 4px solid var(--accent-color-dark);
`;

export const ModalHeader = styled(Typography)`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--border-primary);
`;

export const ModalCloseButton = styled.button`
  background: none;
  border: none;
  font-size: 2rem;
  line-height: 1;
  color: var(--text-secondary);
  cursor: pointer;
  transition: var(--transition-fast);

  &:hover {
    color: var(--text-primary);
  }
`;

export const ModalBody = styled(Typography)`
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 1rem;
  color: var(--text-secondary);
  line-height: 1.6;
  margin-bottom: 24px;
`;

export const ModalOkButton = styled.button`
  font-family: inherit;
  padding: 10px 24px;
  border-radius: var(--radius-md);
  border: none;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  background: var(--accent-gradient);
  color: var(--text-on-dark);
  box-shadow: var(--shadow-sm);
  transition: var(--transition-fast);

  &:hover {
    transform: translateY(-1px);
    box-shadow: var(--shadow-md);
  }
`;

type ModalType = 'error' | 'success' | 'info';

export const ModalIconWrapper = styled(Typography)<{ type?: ModalType }>`
  position: relative;
  flex-shrink: 0;
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border-color: red;

  svg {
    width: 32px;
    height: 32px;
  }

  ${({ type }) =>
    type === 'error' &&
    `
            color: #EF5350;
            box-shadow:inset 0 0 5px 5px rgb(254, 226, 226);
        `}

  ${({ type }) =>
    type === 'info' &&
    `
          box-shadow:inset 0 0 10px rgb(116, 192, 252, 0.4);
          color: #74C0FC;
        `}

      ${({ type }) =>
    type === 'success' &&
    `
          box-shadow:inset 0 0 10px rgb(76, 255, 139, 0.5);
          color:rgb(45, 255, 119);
        `}
`;
