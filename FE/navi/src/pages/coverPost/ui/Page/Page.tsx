import { useState, useEffect } from 'react';
import { useUserStore } from '@/shared/store';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { baseApi } from '@/shared/api';
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
  const navi = useNavigate();
  const store = useUserStore();
  const [artists, setArtists] = useState<Artist[]>([]);
  const [selectedArtist, setSelectedArtist] = useState<string | undefined>(
    undefined,
  );
  const [songs, setSongs] = useState<Song[]>([]);
  const [selectedSong, setSelectedSong] = useState<Song | undefined>(undefined);
  const [parts, setParts] = useState<Part[]>([]);
  const [follows, setFollows] = useState<Follow[]>([]);
  const [images, setImages] = useState<string[]>([]);
  const [selectedPart, setSelectedPart] = useState<Part | undefined>(parts[0]);

  // const [parts, setParts] = useState<>([])

  useEffect(() => {
    const fetchArtists = async () => {
      try {
        const response = await axios.get<{
          resultCode: string;
          message: string;
          data: Artist[];
        }>(`${baseApi}/covers/info`, {
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
        }>(`${baseApi}/users/mutualfollow`, {
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
      }>(`${baseApi}/covers/${artistPk}/song`, {
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
    // 가수 변경 시 파트 정보 초기화
    setParts([]);
    setSongs([]);
    setImages([]);
    setSelectedPart(parts[0]);
    setSelectedSong(undefined);
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
        const response = await axios.get(`${baseApi}/covers/${songId}/select`, {
          withCredentials: true,
        });
        if (response.data.resultCode === 'OK') {
          const updatedParts = response.data.data.map((part: Part) => ({
            ...part,
            image: song.image, // 파트의 이미지를 선택된 곡의 이미지로 업데이트
            userId: 0, // 각 파트의 userId 초기화
            userImage: undefined, // 각 파트의 userImage 초기화
            userName: undefined, // 각 파트의 userName 초기화
          }));
          setParts(updatedParts);
          // 파트 이미지들을 모아서 images 상태를 업데이트
          const partImages = updatedParts.map((part: Part) => part.image);
          setImages(partImages);

          // 첫 번째 파트 선택
          setSelectedPart(updatedParts[0]);
          setCurrentIndex(0); // 인덱스도 업데이트
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
    const { id, image, nickname } = user;

    // 이미 사용자 정보가 있는 경우
    if (selectedPart) {
      const updatedParts = parts.map((part) =>
        part.id === selectedPart.id
          ? {
              ...part,
              userId: id,
              userImage: image,
              userName: nickname,
            }
          : part,
      );

      setParts(updatedParts);
    }
  };
  const handleUpload = async () => {
    // userId가 0이 아니고 정의되어 있는 경우에만 userPk를 설정하여 userPartDtos 생성
    const userPartDtos = parts
      .filter((part) => part.userId !== 0 && part.userId !== undefined) // userId가 0이 아니고 정의되어 있는 경우만 필터링
      .map((part) => ({
        userPk: part.userId, // userId가 0이 아니고 정의되어 있는 경우에만 userPk 설정
        partPk: part.id, // 파트의 ID
      }));

    if (userPartDtos.length > 0) {
      const requestBody = JSON.stringify({
        songPk: selectedSong?.id || 0, // 선택한 곡의 ID, 선택된 곡이 없을 경우 0으로 설정
        userPartDtos: userPartDtos, // 사용자와 파트의 ID를 담은 배열
      });

      try {
        const response = await fetch(
          'https://j10d107.p.ssafy.io/api/covers/create',
          {
            method: 'POST',
            body: requestBody,
            // credentials: 'include',
            headers: {
              'Content-Type': 'application/json',
            },
          },
        );
        console.log(requestBody);
        console.log(response);
        if (!response.ok) {
          console.error('Failed to upload cover');
        } else {
          console.log('Cover upload successful');
          navi('/cover');
        }
      } catch (error) {
        console.error('Failed to upload cover:', error);
      }
    } else {
      console.error('선택된 파트 정보가 없습니다');
      window.alert('가수 및 곡을 선택해주세요');
    }
  };
  const [currentIndex, setCurrentIndex] = useState(0);

  // 이전, 다음 캐러셀을 클릭할 때 selectedPart를 업데이트합니다.
  const nextSlide = () => {
    const newIndex = (currentIndex + 1) % parts.length;
    setSelectedPart(parts[newIndex]); // 다음 슬라이드로 이동할 때 선택된 파트 업데이트
    setCurrentIndex(newIndex);
  };

  const prevSlide = () => {
    const newIndex = (currentIndex - 1 + parts.length) % parts.length;
    setSelectedPart(parts[newIndex]); // 이전 슬라이드로 이동할 때 선택된 파트 업데이트
    setCurrentIndex(newIndex);
  };

  return (
    <div className={css.root}>
      <div className={css.mainContent}>
        {/* 파트 선택 왼쪽 */}
        <div className={css.partList}>
          <ul className={css.partUl}>
            {parts.map((part, index) => (
              <li key={index} className={css.partLi}>
                <div className={css.partContainer}>
                  <div className={css.partDiv}>
                    <img
                      src={part.image}
                      alt={part.name}
                      className={css.partImage}
                    />
                    <p>{part.name}</p>
                  </div>
                  {part.userImage && (
                    <div className={css.profileContainer}>
                      <img
                        src={part.userImage}
                        alt={`${part.name}의 프로필 사진`}
                        className={css.profilePic}
                      />
                      <p>{part.userName}</p>
                    </div>
                  )}
                </div>
              </li>
            ))}
          </ul>
        </div>
        {/* 가운데 카로셀 */}
        <div className={css.carouselContainer}>
          <button onClick={handleUpload} className={css.uploadButton}>
            업로드
          </button>
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
        {/* 친구 고르기 오른쪽 */}
        <div className={css.followListContainer}>
          {/* '나' 추가 */}
          <div className={css.followItem}>
            <div
              className={css.profilePicContainer}
              onClick={() =>
                handleUserClick({
                  id: store.userId,
                  image: store.image,
                  nickname: store.nickname, // '나'라는 닉네임 설정
                  email: '', // 필요에 따라 초기값 설정
                  followingCount: 0, // 필요에 따라 초기값 설정
                  followerCount: 0, // 필요에 따라 초기값 설정
                  username: '', // 필요에 따라 초기값 설정
                  role: '', // 필요에 따라 초기값 설정
                })
              }
            >
              <img
                src={store.image}
                alt="내 프로필 사진"
                className={css.profilePic}
              />
            </div>
            <div className={css.friendName}>나</div>
          </div>

          {/* 나머지 Follow 리스트 */}
          {follows.map((follow, index) => {
            // ROLE_GUEST가 아닌 경우에만 리스트에 추가
            if (follow.role !== 'ROLE_GUEST') {
              return (
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
              );
            }
          })}
        </div>
      </div>
    </div>
  );
}
