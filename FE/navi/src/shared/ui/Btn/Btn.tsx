import { MouseEventHandler } from 'react';
import { Link } from 'react-router-dom';
import { baseUrl } from '@/shared/url';

interface ButtonProps {
  to?: string;
  onClick?: MouseEventHandler<HTMLButtonElement>;

  className: string;
  icon?: string;
  content: string;
}

export function Btn(props: ButtonProps) {
  if (props.to) {
    return (
      <Link to={props.to}>
        <button className={props.className}>
          {props.icon && <img src={`${baseUrl}${props.icon}`} alt="icon" />}
          <span>{props.content}</span>
        </button>
      </Link>
    );
  } else {
    return (
      <button className={props.className} onClick={props.onClick}>
        {props.icon && <img src={`${baseUrl}${props.icon}`} alt="icon" />}
        <span>{props.content}</span>
      </button>
    );
  }
}
