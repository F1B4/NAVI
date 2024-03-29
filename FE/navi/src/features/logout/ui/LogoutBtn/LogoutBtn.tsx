import { Btn } from '@/shared/ui';
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
      <Btn className={css.btn} content="로그아웃" onClick={handleLogOut}></Btn>
    );
  }
  return null;
}
