import { Link } from 'react-router-dom';
import css from './Alarm.module.css';

export function Alarm() {
  return (
    <Link to={'/noraebang'}>
      <div className={css.root}></div>
    </Link>
  );
}
