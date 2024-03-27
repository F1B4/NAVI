interface ButtonProps {
  onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
  // icon?: string;
  className: string;
}

export function Btn(props: ButtonProps) {
  return <button className={props.className} onClick={props.onClick}></button>;
}
