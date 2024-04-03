// import { usePlayStore } from '@/shared/store/playStore/playStore';
// interface CardProps {
//   id: number;
//   type: string;
//   record?: string;
//   video?: string;
//   classCard: string;
//   classImg: string;
//   classDesc: string;
//   thumbnail?: string;
//   user?: string;
//   info: {
//     id: number;
//     title: string;
//     image: string;
//     mr?: string;
//     users?: string;
//   };
// }

// export function Card(props: CardProps) {
//   const store = usePlayStore();

//   const handleClick = () => {
//     store.goPlay({
//       type: props.thumbnail ? 'cover' : 'noraebang',
//       url: (props.video ?? props.record) || '',
//       title: props.info.title,
//       coverImage: props.thumbnail ? props.thumbnail : props.info.image,
//       artist: props.user,
//     });
//   };

//   const imageSrc = props.thumbnail ? props.thumbnail : props.info.image;
//   return (
//     <div className={props.classCard}>
//       <div className={props.classImg} onClick={handleClick}>
//         <img src={imageSrc} alt="image" />
//       </div>
//       <div className={props.classDesc} onClick={handleClick}>
//         <strong>{props.info.title}</strong>
//         {props.user}
//       </div>
//     </div>
//   );
// }

import { usePlayStore } from '@/shared/store/playStore/playStore';
import { useState } from 'react';

interface CardProps {
  id: number;
  type: string;
  record?: string;
  video?: string;
  classCard: string;
  classImg: string;
  classDesc: string;
  thumbnail?: string;
  user?: string;
  info: {
    id: number;
    title: string;
    image: string;
    mr?: string;
    users?: string;
  };
}

export function Card(props: CardProps) {
  const store = usePlayStore();
  const [isHovered, setIsHovered] = useState(false);

  const handleClick = () => {
    store.goPlay({
      pk: props.id,
      type: props.thumbnail ? 'cover' : 'noraebang',
      url: (props.video ?? props.record) || '',
      title: props.info.title,
      coverImage: props.thumbnail ? props.thumbnail : props.info.image,
      artist: props.user,
    });
  };

  const imageSrc = props.thumbnail ? props.thumbnail : props.info.image;

  const overlayPlayIconStyles: React.CSSProperties = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 50,
    height: 50,
    opacity: isHovered ? 1 : 0,
    transition: 'opacity 0.3s ease',
    cursor: 'pointer',
    zIndex: 1,
    color: '#fff',
  };

  const overlayBackgroundStyles: React.CSSProperties = {
    position: 'absolute',
    top: 0,
    left: 0,
    width: '100%',
    height: '100%',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    opacity: isHovered ? 1 : 0,
    transition: 'opacity 0.3s ease',
    zIndex: 0,
  };

  return (
    <div
      className={props.classCard}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      <div
        className={props.classImg}
        onClick={handleClick}
        style={{ position: 'relative', cursor: 'pointer' }}
      >
        <img src={imageSrc} alt="image" />
        <div style={overlayBackgroundStyles}></div>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 24 24"
          fill="white"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
          style={overlayPlayIconStyles}
          className="overlayPlayIcon"
        >
          <polygon points="5 3 19 12 5 21 5 3" />
        </svg>
      </div>
      <div className={props.classDesc} onClick={handleClick}>
        <strong>{props.info.title}</strong>
        {props.user}
      </div>
    </div>
  );
}
