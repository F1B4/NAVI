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

export function CoverPostPage() {
  const [artists, setArtists] = useState<Artist[]>([]);
  const [selectedArtist, setSelectedArtist] = useState<string | undefined>(
    undefined,
  );
  const [songs, setSongs] = useState<Song[]>([]);
  const [selectedSong, setSelectedSong] = useState<Song | undefined>(undefined);
  const [parts, setParts] = useState<Part[]>([]);
  const [follows, setFollows] = useState<Follow[]>([]);
  const [images, setImages] = useState<String[]>([]);
  const [selectedPart, setSelectedPart] = useState<Part>();

  // const [parts, setParts] = useState<>([])

  useEffect(() => {
    const fetchArtists = async () => {
      try {
        const response = await axios.get<{
          resultCode: string;
          message: string;
          data: Artist[];
        }>('http://localhost:8081/api/covers/info', {
          withCredentials: true,
        });
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
        }>('http://localhost:8081/api/users/mutualfollow', {
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
      }>(`http://localhost:8081/api/covers/${artistPk}/song`, {
        withCredentials: true,
      });
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
          `http://localhost:8081/api/covers/${songId}/select`,
          {
            withCredentials: true,
          },
        );
        if (response.data.resultCode === 'OK') {
          const updatedParts = response.data.data.map((part: Part) => ({
            ...part,
            image: song.image, // 파트의 이미지를 선택된 곡의 이미지로 업데이트
          }));
          setParts(updatedParts);

          // 파트 이미지들을 모아서 images 상태를 업데이트
          const partImages = updatedParts.map((part: Part) => part.image);
          setImages(partImages);
        } else {
          console.error(
            '파트 정보를 가져오는 중 오류 발생:',
            response.data.message,
          );
        }
      } catch (error) {
        console.error('파트 정보를 가져오는 중 오류 발생:', error);
      }
    } else {
      console.error('song 선택 오류');
    }
  };

  // 사진 클릭하면 userdata => partdata에 할당하기
  const handleUserClick = (user: Follow) => {
    const { id, image, nickname } = user; // 클릭된 사용자의 데이터 추출
    const updatedPart: Part = { id, image, name: nickname }; // 클릭된 사용자의 데이터를 파트 형식으로 변환
    setParts([...parts, updatedPart]); // 파트 데이터에 클릭된 사용자의 데이터 추가
  };

  // 등록 클릭하면 partdata 가지고 올리기

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
        {/* 파트 선택 왼쪽 */}
        <div className={css.partList}>
          <ul>
            {parts.map((part, index) => (
              <li key={index}>
                <img src={part.image} alt={part.name} />{' '}
                {/* 파트 이미지 표시 */}
                {part.name} {/* 파트 이름 표시 */}
                {part.userImage && ( // 사용자 이미지가 존재하면 함께 표시
                  <div className={css.profilePicContainer}>
                    <img
                      src={part.userImage}
                      alt={`${part.name}의 프로필 사진`}
                      className={css.profilePic}
                    />
                  </div>
                )}
              </li>
            ))}
          </ul>
        </div>
        {/* 가운데 카로셀 */}
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
        {/* 친구 고르기 오른쪽 */}
        <div className={css.followListContainer}>
          {follows.map((follow, index) => (
            <div key={index} className={css.followItem}>
              <div
                className={css.profilePicContainer}
                onClick={() => handleUserClick(follow)}
              >
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
