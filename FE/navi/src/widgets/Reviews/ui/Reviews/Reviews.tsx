import { MouseEventHandler } from 'react';
import { useUserStore } from '@/shared/store';
import { Btn } from '@/shared/ui';
import { UserImage } from '@/shared/ui';
import { baseUrl } from '@/shared/url';
import axios from 'axios';
import css from './Reviews.module.css';

interface ReviewsProps {
  type: string;
  data?: ReviewInfo[];
}

interface ReviewInfo {
  id: number;
  content: string;
  userId: number;
  nickname: string;
  image: string;
}

interface DeleteProps {
  type: string;
  reviewId: number;
}

export function Reviews(props: ReviewsProps) {
  const store = useUserStore();
  const currentType = props.type;
  const ReviewDelete = ({
    type,
    reviewId,
  }: DeleteProps): MouseEventHandler<HTMLButtonElement> => {
    return () => {
      axios.post(`${baseUrl}/${type}/review/${reviewId}`);
    };
  };

  return (
    <div>
      <div className={css.root}>
        <UserImage className={css.user} image={store.image}></UserImage>
        댓글 입력창,
        <Btn className={css.Btn} content="등록"></Btn>
      </div>

      <div>
        {props.data && props.data.length > 0 ? (
          props.data.map((review, index) => (
            <div key={index} className={css.root}>
              <div>
                <UserImage
                  to={review.userId}
                  className={css.img}
                  image={review.image}
                />
              </div>
              <div>
                <div>{review.nickname}</div>
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
