import { useEffect, useState } from 'react';
import { useUserStore } from '@/shared/store';
import { baseApi } from '@/shared/api';

const SSEComponent = () => {
  const [notification, setNotification] = useState('');
  const store = useUserStore();

  useEffect(() => {
    const eventSource = new EventSource(
      `${baseApi}/notification/subscribe/${store.userId}`,
    ); // SSE 엔드포인트 경로
    eventSource.onmessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.event === 'notification') {
        setNotification(data.data);
        console.log('setNoti');
      }
    };

    return () => {
      eventSource.close(); // 컴포넌트가 언마운트될 때 EventSource 연결을 닫습니다.
    };
  }, []);

  return (
    <div>
      <p>Notification: {notification}</p>
    </div>
  );
};

export default SSEComponent;
