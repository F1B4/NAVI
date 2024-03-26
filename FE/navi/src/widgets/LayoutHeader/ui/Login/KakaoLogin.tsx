import css from './KakaoLogin.module.css';

const onKakaoLogin = () => {
  window.location.href =
    'https://j10d107.p.ssafy.io/api/oauth2/authorization/kakao';
};

const getData = () => {
  fetch('https://j10d107.p.ssafy.io/api/users/info', {
    method: 'GET',
    credentials: 'include',
  })
    .then((res) => res.json())
    .then((data) => {
      console.log(data);
    })
    .catch((error) => alert(error));
};

export function KakaoLogin() {
  return (
    <>
      <div onClick={onKakaoLogin} className={css.root}></div>
    </>
  );
}
