import { UserImage } from '@/shared/ui';
import { Btn } from '@/shared/ui';
import css from './Info.module.css';

interface InfoProps {
  isMe: boolean;
  image: string;
  name: string;
  followingCount: number;
  followerCount: number;
  isFollow: boolean;
}

export function Info(props: InfoProps) {
  return (
    <div className={css.root}>
      <div>
        <UserImage className={css.img} image={props.image} />
        <div>유저 이미지 변경버튼</div>
      </div>
      <div className={css.container}>
        <div className={css.name}>{props.name}</div>
        <div>유저 이름 변경버튼</div>
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
