import { useForm, SubmitHandler } from 'react-hook-form';
import PropTypes from 'prop-types';
import { baseApi } from '@/shared/api';
import { z } from 'zod'; // zod에서 필요한 모듈 가져오기
import css from './Review.module.css';
type CommentFormData = {
  content: string;
  pk: number;
};

// zod 스키마 생성
const commentSchema = z.object({
  content: z.string().min(5), // 댓글 내용은 최소 5글자 이상이어야 함
});

interface CommentFormProps {
  pk: number;
}

const CommentForm: React.FC<CommentFormProps> = ({ pk }) => {
  const { register, handleSubmit } = useForm<CommentFormData>();
  const onSubmit: SubmitHandler<CommentFormData> = async (data) => {
    try {
      // 데이터 유효성 검사
      commentSchema.parse(data);
      CommentForm.propTypes = {
        pk: PropTypes.number.isRequired,
      };
      const cover_pk = pk;
      // 서버로 데이터를 보냅니다.
      const response = await fetch(`${baseApi}/covers/${cover_pk}/review`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: data.content,
      });

      if (response.ok) {
        console.log('댓글이 성공적으로 작성되었습니다.');
      } else {
        console.error('댓글을 작성하는 도중 오류가 발생했습니다.');
      }
    } catch (error) {
      if (error instanceof z.ZodError) {
        console.error('댓글 작성 중 오류가 발생했습니다.', error.errors);
      } else {
        console.error('댓글 작성 중 오류가 발생했습니다.', error);
      }
    }
  };

  return (
    <div className={css.root}>
      <form
        onSubmit={handleSubmit(onSubmit)}
        style={{
          display: 'flex',
          flexDirection: 'row',
          alignItems: 'center',
          width: '100%',
        }}
      >
        <input
          type="text"
          {...register('content')}
          placeholder="댓글 내용을 입력하세요"
          style={{
            flex: '1',
            padding: '10px',
            marginRight: '10px',
            border: '1px solid #ccc',
            borderRadius: '10px',
            fontSize: '16px',
            color: 'black',
            backgroundColor: '#f2f2f2',
          }}
          className={css.input} // 새로운 클래스 추가
        />
        <button
          type="submit"
          className={css.btn}
          style={{ padding: '10px 20px' }}
        >
          댓글 작성
        </button>
      </form>
    </div>
  );
};

export default CommentForm;
