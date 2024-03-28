import { Link } from 'react-router-dom';

interface ButtonProps {
  to: string;
  className: string;
  icon?: string;
  content: string;
}

export function Btn(props: ButtonProps) {
  return (
    <Link to={props.to}>
      <button className={props.className}>
        {props.icon && <img src={props.icon} alt="icon" />}
        <span>{props.content}</span>
      </button>
    </Link>
  );
}
