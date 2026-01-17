import React, { FC, ReactNode } from 'react';
import {
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalCloseButton,
  ModalBody,
  ModalOkButton,
  ModalIconWrapper,
} from './Modal.styles.ts';
import InfoOutlineIcon from '@mui/icons-material/InfoOutline';
import ErrorOutlineOutlinedIcon from '@mui/icons-material/ErrorOutlineOutlined';
import CheckCircleOutlinedIcon from '@mui/icons-material/CheckCircleOutlined';
import { Grid, Typography } from '@mui/material';

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  children: ReactNode;
  type?: 'error' | 'success' | 'info';
}

export const Modal: FC<ModalProps> = ({ isOpen, onClose, title, children, type }) => {
  if (!isOpen) {
    return null;
  }

  function getIcon(type: string) {
    switch (type) {
      case 'error':
        return <ErrorOutlineOutlinedIcon />;
      case 'info':
        return <InfoOutlineIcon />;
      case 'success':
        return <CheckCircleOutlinedIcon />;
      default:
        return <p>...</p>;
    }
  }

  return (
    <ModalOverlay onClick={onClose}>
      <ModalContent onClick={(e) => e.stopPropagation()}>
        <ModalHeader>
          <Typography variant="h6" fontWeight="600">
            {title}
          </Typography>
          <ModalCloseButton onClick={onClose} title="Close">
            &times;
          </ModalCloseButton>
        </ModalHeader>
        <ModalBody>
          <ModalIconWrapper type={type}>{getIcon(type ?? '')}</ModalIconWrapper>
          <div>{children}</div>
        </ModalBody>
        <Grid display="flex" justifyContent="flex-end">
          <ModalOkButton onClick={onClose}>OK</ModalOkButton>
        </Grid>
      </ModalContent>
    </ModalOverlay>
  );
};
