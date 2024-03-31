import { useUserStore } from '@/shared/store';
import css from './Login.module.css';

const onNaverLogin = () => {
  window.location.href =
    'https://j10d107.p.ssafy.io/api/oauth2/authorization/naver';
  // 'http://localhost:8081/api/oauth2/authorization/naver';
};

const onGoogleLogin = () => {
  window.location.href =
    'https://j10d107.p.ssafy.io/api/oauth2/authorization/google';
  // 'http://localhost:8081/api/oauth2/authorization/google';
};

const onKakaoLogin = () => {
  window.location.href =
    'https://j10d107.p.ssafy.io/api/oauth2/authorization/kakao';
  // 'http://localhost:8081/api/oauth2/authorization/kakao';
};

export function Login() {
  const store = useUserStore();
  if (!store.isLogin) {
    return (
      <>
        <div onClick={onNaverLogin} className={css.naver}></div>
        <div onClick={onKakaoLogin} className={css.kakao}></div>
        <div onClick={onGoogleLogin} className={css.google}></div>
      </>
    );
  }
  return null;
}
