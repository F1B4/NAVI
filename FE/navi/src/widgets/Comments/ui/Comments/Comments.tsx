import { useUserStore } from '@/shared/store';
import { Btn } from '@/shared/ui';
import { UserImage } from '@/shared/ui';
import css from './Comments.module.css';

export function Comments() {
  const store = useUserStore();
  return (
    <div>
      <div>
        <UserImage className={css.img} image={store.image}></UserImage>
        댓글 입력창,
        <Btn className={css.Btn} content="등록"></Btn>
      </div>
      <div>
        v-for 댓글들
        <div>
          <div>작성자 이미지</div>
          <div>작성자 닉네임, 작성 내용, 작성자가 나라면 삭제버튼</div>
        </div>
      </div>
    </div>
  );
}
