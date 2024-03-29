import { useNavigate } from 'react-router-dom';

interface UserImageProps {
  to?: number;
  className: string;
  image: string;
}

export function UserImage(props: UserImageProps) {
  const navi = useNavigate();

  const goProfile = () => {
    navi(`/profile/${props.to}`);
  };
  return (
    <div className={props.className} onClick={goProfile}>
      <img src={props.image} alt="userImage" />
    </div>
  );
}
