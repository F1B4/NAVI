import { useEffect } from 'react';
import { useUserStore, useNotiStore } from '@/shared/store';
import { baseApi } from '@/shared/api';

const SSEComponent = () => {
  const noti = useNotiStore();
  const store = useUserStore();

  useEffect(() => {
    const eventSource = new EventSource(
      `${baseApi}/sse/notification/subscribe/${store.userId}`,
    );

    eventSource.addEventListener('open', function () {
      console.log('Connection opened');
    });

    // 알람이 왔음
    eventSource.addEventListener('notification', function (event) {
      console.log('Notification received:', event.data);
      noti.getAlarms();
    });

    eventSource.addEventListener('error', function () {
      console.log('Error occurred');
    });

    return () => {
      console.log('Cleaning up...');
      eventSource.close(); // 컴포넌트가 언마운트되면 SSE 연결 종료
    };
  }, [store]);
};
export default SSEComponent;
