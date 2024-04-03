import { Link } from 'react-router-dom';
import css from './Logo.module.css';
import { usePlayStore } from '@/shared/store';

export function Logo() {
  const { expand } = usePlayStore(); // expand 함수 가져오기

  const handleLogoClick = () => {
    expand(); // expand 함수 호출하여 expand 상태를 false로 변경
    console.log();
  };

  return (
    <Link to={'/'} onClick={handleLogoClick}>
      <div className={css.root}></div>
    </Link>
  );
}
