import { MouseEventHandler } from 'react';
import { useUserStore } from '@/shared/store';
import { Btn } from '@/shared/ui';
import { baseUrl } from '@/shared/url';
import axios from 'axios';
import css from './Reviews.module.css';
import CommentForm from '@/features/review/Review';

interface User {
  id: number;
  nickname: string;
  email: string;
  image: string;
  followingCount: number;
  followerCount: number;
}

interface Review {
  id: number;
  content: string;
  userDto?: User;
  userId?: number;
  nickname?: string;
  image?: string;
}

interface ReviewsProps {
  pk: number;
  type: string; // 타입을 여기서 고정
  data?: Review[]; // Review 타입을 사용합니다.
}

interface DeleteProps {
  type: string;
  reviewId: number;
}

import { FC } from 'react';

interface UserImageProps {
  to?: string | number | undefined;
  className?: string;
  image?: string;
}

const UserImage: FC<UserImageProps> = ({ to, className, image }) => {
  return (
    <img
      src={image || '/default-image.png'} // 이미지가 없을 경우에 대비한 기본 이미지 URL 설정
      alt="User Image"
      className={className}
      onClick={() => {
        if (to) {
          // 이미지 클릭 시 to 속성이 주어진 경우 해당 링크로 이동
          window.location.href = String(to);
        }
      }}
    />
  );
};

export default UserImage;

export function Reviews(props: ReviewsProps) {
  const store = useUserStore();
  const currentType = props.type;

  const ReviewDelete = ({
    type,
    reviewId,
  }: DeleteProps): MouseEventHandler<HTMLButtonElement> => {
    return () => {
      axios.post(`${baseUrl}/${type}/review/${reviewId}`, {
        withCredential: true,
      });
    };
  };
  console.log(Reviews);
  return (
    <div>
      {store.isLogin ? (
        <div className={css.root}>
          <UserImage image={store.image} className={css.img}></UserImage>
          <CommentForm pk={props.pk} />
        </div>
      ) : (
        <div>댓글을 작성하려면 로그인해주세요</div>
      )}
      <div>
        {props.data && props.data.length > 0 ? (
          props.data.map((review, index) => (
            <div key={index} className={css.root}>
              <div>
                <UserImage
                  to={
                    currentType === 'covers'
                      ? review.userDto?.id
                      : review.userId
                  }
                  className={css.img}
                  image={
                    currentType === 'covers'
                      ? review.userDto?.image
                      : review.image
                  }
                />
              </div>
              <div>
                <div>
                  {currentType === 'covers'
                    ? review.userDto?.nickname
                    : review.nickname}
                </div>
                <div>{review.content}</div>
                {review.userId === store.userId && (
                  <Btn
                    className={css.Btn}
                    content="삭제"
                    onClick={ReviewDelete({
                      type: currentType,
                      reviewId: review.id,
                    })}
                  ></Btn>
                )}
              </div>
            </div>
          ))
        ) : (
          <div>댓글이 없습니다.</div>
        )}
      </div>
    </div>
  );
}
