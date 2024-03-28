interface UserImageProps {
  onClick?: (e: React.MouseEvent<HTMLDivElement>) => void;
  className: string;
  image: string;
}

export function UserImage(props: UserImageProps) {
  return (
    <div className={props.className} onClick={props.onClick}>
      <img src={props.image} alt="userImage" />
    </div>
  );
}
