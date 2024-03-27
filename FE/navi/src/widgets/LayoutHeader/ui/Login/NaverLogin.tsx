import css from './NaverLogin.module.css';

const onNaverLogin = () => {
  window.location.href = 'http://localhost:8081/api/oauth2/authorization/naver';
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

export function NaverLogin() {
  return (
    <>
      <div onClick={onNaverLogin} className={css.root}></div>
    </>
  );
}
