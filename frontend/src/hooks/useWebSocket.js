import { useEffect, useRef } from 'react';
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const useWebSocket = (WS_URL, onMessage) => {
  const stompClientRef = useRef(null);

  useEffect(() => {
    const socket = new SockJS(WS_URL);
    const client = Stomp.over(socket);

    client.connect(
      {},
      (frame) => {
        console.log('Connected: ' + frame);
        client.subscribe('/topic/bookings', (message) => {
          if (onMessage) {
            onMessage(JSON.parse(message.body));
          }
        });
      },
      (error) => {
        console.error('WebSocket Error:', error);

        setTimeout(() => {
          if (!stompClientRef.current?.connected) {
            client.activate();
          }
        }, 5000);
      }
    );

    stompClientRef.current = client;

    return () => {
      if (stompClientRef.current) {
        stompClientRef.current.disconnect(() => {
          console.log('WebSocket Disconnected');
        });
      }
    };
  }, [WS_URL, onMessage]);
};

export default useWebSocket;
