import { useState } from 'react';
import css from './Page.module.css';

const partList = ['파트1', '파트2', '파트3'];
const followList = [
  {
    name: '철수',
    img: 'https://pbs.twimg.com/profile_images/578040448108752896/oQK7a3gS_400x400.jpeg',
  },
  {
    name: '유리',
    img: 'https://mblogthumb-phinf.pstatic.net/MjAyMzAyMTNfMTQx/MDAxNjc2Mjg5NzI2NTQ5.venb_kM6AyNAbIwHJV_f4SYaaQNg3FnbEPGS2rVArxYg.wJvAVifKAxeiJ2JP-3aYXk1JSNH7AOVc45q6vPobvTcg.JPEG.jyw711/output_1463835467.jpg?type=w800',
  },
  {
    name: '맹구',
    img: 'https://media.bunjang.co.kr/product/254840755_1_1709027535_w360.jpg',
  },
];

const images = [
  'https://mblogthumb-phinf.pstatic.net/MjAxNzA4MDJfMjU3/MDAxNTAxNjY2MTc3NzIy.e0oxqWD-DcS5qA34E5-T0PQ5wsoCGI37fYXMt8HpfkMg.O6cic22hPdu169R5C3AnJtNNvSwnpTj3FRYBOs0n878g.JPEG.iamsunny95/696563CD-E693-48D3-B7C8-01ACA3368767-26640-000012C698E2FF12_file.jpg?type=w800',
  'https://blog.kakaocdn.net/dn/2cxt0/btsbBO1CP26/isxzWiFSQmCprbdA8Qy3xK/img.jpg',
  'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSsBiGQaTZcxBCFHuvadM9-J1a1jycHBLxfPV7if20ahf_ZgrajOTEt3sYMUe5y4bebb2Q&usqp=CAU',
];

export function CoverPostPage() {
  const [currentIndex, setCurrentIndex] = useState(0);

  const nextSlide = () => {
    setCurrentIndex((currentIndex + 1) % images.length);
  };
  const prevSlide = () => {
    setCurrentIndex((currentIndex - 1 + images.length) % images.length);
  };

  return (
    <div className={css.root}>
      <div className={css.mainContent}>
        <div className={css.partList}>
          <ul>
            {partList.map((part, index) => (
              <li key={index}>{part}</li>
            ))}
          </ul>
        </div>
        <div className={css.carouselContainer}>
          <div className={css.title}>커버 선택</div>
          <div className={css.coverSelectSection}>
            <div className={css.dropdownContainer}>
              <select className={css.dropdown}>
                <option value="가수1">부석순</option>
                <option value="가수2">트와이스</option>
                <option value="가수3">투마로우바이투게더</option>
                {/* 추가 옵션... */}
              </select>
              <select className={css.dropdown}>
                <option value="cover1">파이팅 해야지</option>
                <option value="cover2">거침없이</option>
                <option value="cover3">7시에 들어줘</option>
                {/* 추가 옵션... */}
              </select>
            </div>
          </div>
          <div className={css.carouselControls}>
            <button onClick={prevSlide} className={css.controlButton}>
              ◀
            </button>
            <img
              src={images[currentIndex]}
              alt="slide"
              className={css.imageSlide}
            />
            <button onClick={nextSlide} className={css.controlButton}>
              ▶
            </button>
          </div>
          <div className={css.btn}>
            {images.map((_, index) => (
              <span
                key={index}
                className={`${css.indicator} ${
                  index === currentIndex ? css.indicatorActive : ''
                }`}
                onClick={() => setCurrentIndex(index)}
              >
                ●
              </span>
            ))}
          </div>
        </div>
        <div className={css.followListContainer}>
          {followList.map((friend, index) => (
            <div key={index} className={css.followItem}>
              <div className={css.profilePicContainer}>
                <img
                  src={friend.img}
                  alt={`${friend.name}의 프로필 사진`}
                  className={css.profilePic}
                />
              </div>
              <div className={css.friendName}>{friend.name}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
