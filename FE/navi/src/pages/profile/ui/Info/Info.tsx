import { UserImage } from '@/shared/ui';
import css from './Info.module.css';

interface InfoProps {
  image: string;
  name: string;
  followingCount: number;
  followerCount: number;
  isFollow: boolean;
}

export function Info(props: InfoProps) {
  return (
    <div>
      <UserImage className={css.img} image={props.image} />
      <div>유저 이미지 변경버튼</div>
      <div>
        {props.name}
        <div>유저 이름 변경버튼</div>
        <div>
          <div>
            {props.followingCount}
            팔로잉
          </div>
          <div>
            {props.followerCount}
            팔로워
          </div>
        </div>
      </div>
    </div>
  );
}
