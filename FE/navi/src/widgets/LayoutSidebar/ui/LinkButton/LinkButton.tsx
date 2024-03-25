import React from 'react';
import { Link } from 'react-router-dom';
import css from './LinkButton.module.css';

interface ButtonProps {
  to: string;
  children: React.ReactNode;
}

function Button({ to, children }: ButtonProps) {
  return (
    <Link to={to} className={css.root}>
      {children}
    </Link>
  );
}

export default Button;
