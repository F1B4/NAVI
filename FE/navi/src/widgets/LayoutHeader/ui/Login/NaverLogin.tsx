import css from './NaverLogin.module.css';

const onNaverLogin = () => {
  window.location.href = 'http://localhost:8081/api/oauth2/authorization/naver';
};

const getData = () => {
  fetch('http://localhost:8081/api/users/info', {
    method: 'GET',
    credentials: 'include',
  })
    .then((data) => {
      console.log(data);
    })
    .catch((error) => alert(error));
};

export function NaverLogin() {
  return (
    <>
      <div onClick={getData} className={css.root}></div>
    </>
  );
}
