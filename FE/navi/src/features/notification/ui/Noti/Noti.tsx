import { useEffect, useState } from 'react';
import { useUserStore } from '@/shared/store';
import { baseApi } from '@/shared/api';

const SSEComponent = () => {
  const [notification, setNotification] = useState('');
  const store = useUserStore();

  useEffect(() => {
    const eventSource = new EventSource(
      `${baseApi}/sse/notification/subscribe/${store.userId}`,
    );

    eventSource.addEventListener('open', function () {
      console.log('Connection opened');
    });

    eventSource.addEventListener('notification', function (event) {
      console.log('Notification received:', event.data);
      setNotification(event.data); // 이벤트 데이터를 상태에 업데이트
    });

    eventSource.addEventListener('error', function () {
      console.log('Error occurred');
    });

    return () => {
      console.log('Cleaning up...');
      eventSource.close(); // 컴포넌트가 언마운트되면 SSE 연결 종료
    };
  }, [store]);

  return (
    <div>
      <p>Notification: {notification}</p>
    </div>
  );
};
export default SSEComponent;
