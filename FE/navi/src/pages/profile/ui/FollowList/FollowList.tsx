import { UserImage } from '@/shared/ui';
// import { Btn } from '@/shared/ui';
import css from './FollowList.module.css';

interface User {
  id: number;
  nickname: string;
  email: string;
  image: string;
  followingCount: number;
  followerCount: number;
  username: string;
  role: string;
}

interface Follow {
  id: number;
  user: User;
}

interface FollowProps {
  ing: Follow[];
  er: Follow[];
}

export function FollowList(props: FollowProps) {
  console.log(props);
  return (
    <div className={css.root}>
      <div className={css.ing}>
        <div className={css.title}>팔로잉</div>
        {props.ing.length > 0 ? (
          Array.isArray(props.ing) &&
          props.ing.map((ing, index) => (
            <div className={css.user} key={index}>
              <UserImage
                to={ing.user.id}
                className={css.user}
                image={ing.user.image}
              />
              {ing.user.nickname}
            </div>
          ))
        ) : (
          <div className={css.content}>팔로잉이 없습니다</div>
        )}
      </div>
      <div className={css.er}>
        <div className={css.title}>팔로워</div>
        {props.er.length > 0 ? (
          Array.isArray(props.er) &&
          props.er.map((er, index) => (
            <div className={css.user} key={index}>
              <UserImage
                to={er.user.id}
                className={css.user}
                image={er.user.image}
              />
              {er.user.nickname}
            </div>
          ))
        ) : (
          <div className={css.content}>팔로워가 없습니다</div>
        )}
      </div>
    </div>
  );
}
