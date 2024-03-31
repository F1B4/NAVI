import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { profileApi } from '@/entities/profile';
import type { Profile } from '@/entities/profile';
import { Info } from '../Info/Info';
// import { LikeList } from '../LikeList/LikeList';
import { MySongList } from '../MySongList/MySongList';
import css from './Page.module.css';

export function ProfilePage() {
  const { userPk } = useParams();
  const props = Number(userPk);
  const [profile, setProfile] = useState<Profile>();
  const [load, setLoad] = useState<boolean>(false);

  useEffect(() => {
    const AxiosProfile = async () => {
      try {
        const response = await profileApi(props);
        if (response?.resultCode === 'OK') {
          setProfile(response.data);
          setLoad(true);
        }
      } catch (error) {
        console.error('Error get profile');
      }
    };
    AxiosProfile();
  }, []);

  if (load && profile) {
    return (
      <div className={css.root}>
        <div className={css.info}>
          <Info
            name={profile.nickname}
            image={profile.image}
            followingCount={profile.followingCount}
            followerCount={profile.followerCount}
            isFollow={profile.isFollow}
          />
        </div>
        <div className={css.listContainer}>
          좋아요 목록
          <div className={css.likeList}>{/* <LikeList /> */}</div>
          내가 부른 노래 목록
          <div className={css.mySongList}>{/* <MySongList /> */}</div>
        </div>
      </div>
    );
  }
}
