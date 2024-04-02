import { useForm, SubmitHandler } from 'react-hook-form';
import { z } from 'zod'; // zod에서 필요한 모듈 가져오기
import { baseApi } from '@/shared/api';

type CommentFormData = {
  content: string;
};

// zod 스키마 생성
const commentSchema = z.object({
  content: z.string().min(5), // 댓글 내용은 최소 5글자 이상이어야 함
});

const CommentForm: React.FC = () => {
  const { register, handleSubmit } = useForm<CommentFormData>();
  const onSubmit: SubmitHandler<CommentFormData> = async (data) => {
    try {
      // 데이터 유효성 검사
      commentSchema.parse(data);

      const cover_pk = 4;
      // 서버로 데이터를 보냅니다.
      const response = await fetch(`${baseApi}/covers/${cover_pk}/review`, {
        method: 'POST',
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
    <form onSubmit={handleSubmit(onSubmit)}>
      <textarea {...register('content')} placeholder="댓글 내용을 입력하세요" />
      <button type="submit">댓글 작성</button>
    </form>
  );
};

export default CommentForm;
