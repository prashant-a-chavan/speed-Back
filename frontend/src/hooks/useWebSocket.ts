import { useEffect, useRef } from 'react';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Booking } from '../types';

const useWebSocket = (wsUrl: string, onMessage: (bookings: Booking[]) => void): void => {
  const stompClientRef = useRef<Client | null>(null);

  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS(wsUrl),
      onConnect: () => {
        console.log('WebSocket connected');
        client.subscribe('/topic/bookings', (message: IMessage) => {
          onMessage(JSON.parse(message.body) as Booking[]);
        });
      },
      onStompError: (frame) => {
        console.error('WebSocket STOMP error:', frame);
      },
      reconnectDelay: 5000,
    });

    client.activate();
    stompClientRef.current = client;

    return () => {
      client.deactivate().then(() => console.log('WebSocket disconnected'));
    };
  }, [wsUrl, onMessage]);
};

export default useWebSocket;

