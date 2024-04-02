import { useState, useEffect } from 'react';
import axios from 'axios';
import css from './Page.module.css';

interface Artist {
  id: number;
  partCount: number;
  name: string;
}

interface Song {
  id: number;
  title: string;
  mr: string;
  image: string;
}

interface Part {
  id: number;
  image: string;
  name: string;
  userId?: number;
  userImage?: string;
  userName?: string;
}

interface Follow {
  id: number;
  nickname: string;
  email: string;
  image: string;
  followingCount: number;
  followerCount: number;
  username: string;
  role: string;
}

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
  const [artists, setArtists] = useState<Artist[]>([]);
  const [selectedArtist, setSelectedArtist] = useState<string | undefined>(
    undefined,
  );
  const [songs, setSongs] = useState<Song[]>([]);
  const [selectedSong, setSelectedSong] = useState<Song | undefined>(undefined);
  const [parts, setParts] = useState<Part[]>([]);
  const [follows, setFollows] = useState<Follow[]>([]);

  // const [parts, setParts] = useState<>([])

  useEffect(() => {
    const fetchArtists = async () => {
      try {
        const response = await axios.get<{
          resultCode: string;
          message: string;
          data: Artist[];
        }>('http://localhost:8081/api/covers/info');
        if (response.data.resultCode === 'OK') {
          setArtists(response.data.data);
        } else {
          console.error(
            '가수 목록을 가져오는 중 오류 발생:',
            response.data.message,
          );
        }
      } catch (error) {
        console.error('가수 목록을 가져오는 중 오류 발생:', error);
      }
    };
    const fetchFollows = async () => {
      try {
        const response = await axios.get<{
          resultCode: string;
          message: string;
          data: Follow[];
        }>('http://localhost:8081/api/users/Follow', {
          withCredentials: true,
        });
        if (response.data.resultCode === 'OK') {
          setFollows(response.data.data);
        } else {
          console.error(
            '맞팔로우 목록을 가져오는 중 오류 발생:',
            response.data.message,
          );
        }
      } catch (error) {
        console.error('맞팔로우 목록을 가져오는 중 오류 발생:', error);
      }
    };

    fetchArtists();
    fetchFollows();
  }, []);

  const fetchSongsByArtist = async (artistPk: number) => {
    try {
      const response = await axios.get<{
        resultCode: string;
        message: string;
        data: Song[];
      }>(`http://localhost:8081/api/covers/${artistPk}/song`);
      if (response.data.resultCode === 'OK') {
        setSongs(response.data.data);
      } else {
        console.error(
          '아티스트의 곡 목록을 가져오는 중 오류 발생:',
          response.data.message,
        );
      }
    } catch (error) {
      console.error('아티스트의 곡 목록을 가져오는 중 오류 발생:', error);
    }
  };

  const handleArtistChange = async (
    event: React.ChangeEvent<HTMLSelectElement>,
  ) => {
    const artistId = parseInt(event.target.value);
    setSelectedArtist(event.target.value);
    if (artistId) {
      await fetchSongsByArtist(artistId);
    } else {
      setSongs([]);
    }
  };

  const handleSongChange = async (
    event: React.ChangeEvent<HTMLSelectElement>,
  ) => {
    const songId = parseInt(event.target.value);
    const song = songs.find((song) => song.id === songId);
    setSelectedSong(song);
    if (song) {
      try {
        const response = await axios.get(
          `http://localhost:8081/api/covers/select/${songId}`,
        );
        if (response.data.resultCode === 'OK') {
          // 여기서 파트랑 팔로우 목록 넣어줘야함
        } else {
          console.error('가사를 가져오는 중 오류 발생:', response.data.message);
        }
      } catch (error) {
        console.error('가사를 가져오는 중 오류 발생:', error);
      }
    } else {
    }
  };

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
              <select
                className={css.dropdown}
                onChange={handleArtistChange}
                value={selectedArtist || ''}
              >
                <option value="" disabled hidden>
                  가수 선택
                </option>
                {artists.map((artist) => (
                  <option key={artist.id} value={artist.id.toString()}>
                    {artist.name}
                  </option>
                ))}
              </select>

              {selectedArtist ? (
                <select
                  className={css.dropdown}
                  onChange={handleSongChange}
                  value={selectedSong ? selectedSong.id.toString() : ''}
                >
                  <option value="" disabled hidden>
                    노래 선택
                  </option>
                  {songs.map((song) => (
                    <option key={song.id} value={song.id.toString()}>
                      {song.title}
                    </option>
                  ))}
                </select>
              ) : (
                <div className={css.dropdown}>가수를 선택해주세요</div>
              )}
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
          {follows.map((follow, index) => (
            <div key={index} className={css.followItem}>
              <div className={css.profilePicContainer}>
                <img
                  src={follow.image}
                  alt={`${follow.nickname}의 프로필 사진`}
                  className={css.profilePic}
                />
              </div>
              <div className={css.friendName}>{follow.nickname}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
