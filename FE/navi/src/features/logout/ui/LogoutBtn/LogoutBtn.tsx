import { logOut } from '../../model/logout';
import { useUserStore } from '@/shared/store';
import { useNavigate } from 'react-router-dom';
import css from './LogoutBtn.module.css';

export function LogOutBtn() {
  const store = useUserStore();
  const navigate = useNavigate();

  const handleLogOut = async () => {
    await logOut(store);
    navigate('/');
  };

  if (store.isLogin) {
    return (
      <img
        className={css.btn}
        src="/images/logout.png" // 이미지 경로 설정
        alt="로그아웃"
        onClick={handleLogOut}
      />
    );
  }
  return null;
}
