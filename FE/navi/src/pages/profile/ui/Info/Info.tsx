import { useState, useRef } from 'react';
import { UserImage } from '@/shared/ui';
import { Btn } from '@/shared/ui';
import css from './Info.module.css';
import { baseApi } from '@/shared/api';
import { useUserStore } from '@/shared/store';
import { useNavigate } from 'react-router-dom';

interface InfoProps {
  isMe: boolean;
  image: string;
  name: string;
  followingCount: number;
  followerCount: number;
  isFollow: boolean;
}

export function Info(props: InfoProps) {
  const [isEditingName, setIsEditingName] = useState(false);
  const [editingName, setEditingName] = useState(props.name);
  const store = useUserStore();
  const navi = useNavigate();
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleIconClick = () => {
    fileInputRef.current?.click();
  };

  const handleFileChange = async (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const files = event.target.files;
    if (!files || files.length === 0) {
      return;
    }
    const file = files[0];

    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await fetch(`${baseApi}/users/image`, {
        method: 'POST',
        body: formData,
        credentials: 'include',
      });
      if (!response.ok) {
        throw new Error('API 호출에 실패했습니다.');
      }

      await store.getData();
      navi(0);
    } catch (error) {
      console.error(error);
    }
  };

  const startEditingName = () => {
    setIsEditingName(true);
  };

  const stopEditingName = async () => {
    try {
      const response = await fetch(`${baseApi}/users/nickname`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: editingName,
        credentials: 'include',
      });
      if (!response.ok) {
        throw new Error('이름 변경 API 호출에 실패했습니다.');
      }

      setIsEditingName(false);
      await store.getData();
      navi(0);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className={css.root}>
      <div className={css.imageContainer}>
        <UserImage className={css.img} image={props.image} />
        {props.isMe && (
          <div onClick={handleIconClick} className={css.btn1}>
            <img
              className={css.modifyIcon}
              src="/images/profile_img_modify.png"
            />
            <input
              type="file"
              ref={fileInputRef}
              onChange={handleFileChange}
              style={{ display: 'none' }}
            />
          </div>
        )}
      </div>
      <div className={css.container}>
        <div className={css.nameContainer}>
          {isEditingName ? (
            <>
              <input
                className={css.editingNameInput}
                type="text"
                value={editingName}
                onChange={(e) => setEditingName(e.target.value)}
              />
              <img
                className={css.nameModifyIcon}
                src="/images/profile_img_modify.png"
                onClick={stopEditingName}
              />
            </>
          ) : (
            <>
              <div className={css.name}>{props.name}</div>
              {props.isMe && (
                <img
                  className={css.nameModifyIcon}
                  src="/images/profile_img_modify.png"
                  onClick={startEditingName}
                />
              )}
            </>
          )}
        </div>
        <div className={css.countContainer}>
          <div className={css.count}>
            {props.followingCount}
            <span>팔로잉</span>
          </div>
          <div className={css.count}>
            {props.followerCount}
            <span>팔로워</span>
          </div>
        </div>
        {!props.isMe && (
          <Btn
            className={css.btn}
            content={props.isFollow ? '언팔로우' : '팔로우'}
          />
        )}
      </div>
    </div>
  );
}
