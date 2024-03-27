interface UserImageProps {
  onClick?: (e: React.MouseEvent<HTMLDivElement>) => void;
  className: string;
}

export function UserImage(props: UserImageProps) {
  return <div className={props.className} onClick={props.onClick}></div>;
}
