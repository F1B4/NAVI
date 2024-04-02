import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { ProfileApi, FollowingApi, FollowerApi } from '@/entities/profile';
import type { Profile, Follow } from '@/entities/profile';
import { useUserStore } from '@/shared/store';
import { Info } from '../Info/Info';
<<<<<<< HEAD
// import { LikeList } from '../LikeList/LikeList';
// import { MySongList } from '../MySongList/MySongList';
=======
import { LikeList } from '../LikeList/LikeList';
import { MySongList } from '../MySongList/MySongList';
import { FollowList } from '../FollowList/FollowList';
>>>>>>> develop
import css from './Page.module.css';

export function ProfilePage() {
  const store = useUserStore();
  const { userPk } = useParams();
  const props = Number(userPk);
  const [profile, setProfile] = useState<Profile>();
  const [following, setFollowing] = useState<Follow[]>([]);
  const [follower, setFollower] = useState<Follow[]>([]);
  const [load, setLoad] = useState<boolean>(false);
  const isMe = props === store.userId ? true : false;

  useEffect(() => {
    const AxiosProfile = async () => {
      try {
        const response = await ProfileApi({
          detailPk: props,
          userId: store.userId,
        });
        if (response?.resultCode === 'OK') {
          setProfile(response.data);
        }
      } catch (error) {
        console.error('Error get profile');
      }
    };
    const AxiosFollowing = async () => {
      try {
        const response = await FollowingApi(props);
        if (response?.resultCode === 'OK') {
          setFollowing(response.data);
        }
      } catch (error) {
        console.error('Error get following');
      }
    };
    const AxiosFollower = async () => {
      try {
        const response = await FollowerApi(props);
        if (response?.resultCode === 'OK') {
          setFollower(response.data);
          setLoad(true);
        }
      } catch (error) {
        console.error('Error get follower');
      }
    };
    AxiosProfile();
    AxiosFollowing();
    AxiosFollower();
    window.scrollTo(0, 0);
  }, [props]);

  if (load && profile) {
    return (
      <div className={css.root}>
        <div className={css.info}>
          <Info
            isMe={isMe}
            name={profile.nickname}
            image={profile.image}
            followingCount={profile.followingCount}
            followerCount={profile.followerCount}
            isFollow={profile.isFollow}
          />
        </div>
        <div className={css.followContainer}>
          <FollowList ing={following} er={follower} />
        </div>
        <div className={css.listContainer}>
          좋아요 목록
          <div className={css.likeList}>
            <LikeList
              likeNorae={profile.noraebangLikeDtos}
              likeCover={profile.coverLikeDtos}
            />
          </div>
          내가 부른 노래 목록
          <div className={css.mySongList}>
            <MySongList
              myNorae={profile.noraebangDtos}
              myCover={profile.coverDtos}
            />
          </div>
        </div>
      </div>
    );
  }
  return null;
}
