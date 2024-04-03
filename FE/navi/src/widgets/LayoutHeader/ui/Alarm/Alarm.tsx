import { useNotiStore } from '@/shared/store';
import { useUserStore } from '@/shared/store';
import { useState } from 'react';
import { baseApi } from '@/shared/api';
import axios from 'axios';
import css from './Alarm.module.css';

export function Alarm() {
  const noti = useNotiStore();
  const store = useUserStore();
  const [isOpen, setIsOpen] = useState(false);

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const handleItemClick = async (id: number) => {
    await axios.delete(`${baseApi}/alarms/delete/${id}`, {
      withCredentials: true,
    });
    noti.getAlarms();
  };

  if (store.isLogin) {
    return (
      <div className={`dropdown dropdown-end dropdown-bottom ${css.dropdown}`}>
        <div
          tabIndex={0}
          role="button"
          className={`${css.root} ${noti.data.length === 0 ? css.root2 : ''}`}
          onClick={noti.data.length > 0 ? toggleDropdown : undefined}
        ></div>
        {isOpen && (
          <ul
            tabIndex={0}
            className={`bg-gray-00 menu dropdown-content z-[1] w-52 rounded-box bg-neutral-600 p-2 shadow ${css.dropdownContent}`}
          >
            {noti.data.map((notification) => (
              <li
                key={notification.id}
                onClick={() => handleItemClick(notification.id)}
              >
                <a
                  className={
                    notification.alarmStatus === 'UNREAD' ? css.unread : ''
                  }
                >
                  {notification.content}
                </a>
              </li>
            ))}
          </ul>
        )}
      </div>
    );
  }
  return null;
}
