import css from './GoogleLogin.module.css';

const onGoogleLogin = () => {
  window.location.href =
    'https://j10d107.p.ssafy.io/api/oauth2/authorization/google';
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

export function GoogleLogin() {
  return (
    <>
      <div onClick={onGoogleLogin} className={css.root}></div>
    </>
  );
}
