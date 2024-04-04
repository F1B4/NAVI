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

const defaultFollow: Follow = {
  id: 0, // 이 값은 실제 상황에 맞게 조정되어야 합니다.
  nickname: 'Default Name', // 기본 닉네임, 실제 값으로 변경해야 합니다.
  email: 'default@example.com', // 기본 이메일, 실제 값으로 변경해야 합니다.
  image:
    'https://navi.s3.ap-northeast-2.amazonaws.com/free-icon-plus-8162972.png', // 주어진 이미지 URL
  followingCount: 0, // 기본 팔로잉 카운트, 실제 값으로 변경해야 합니다.
  followerCount: 0, // 기본 팔로워 카운트, 실제 값으로 변경해야 합니다.
  username: 'defaultuser', // 기본 유저네임, 실제 값으로 변경해야 합니다.
  role: 'Default Role', // 기본 역할, 실제 값으로 변경해야 합니다.
};

interface Follow {
  id: number;
  nickname: string;
  email?: string;
  image: string;
  followingCount: number;
  followerCount: number;
  username?: string;
  role: string;
}

export function CoverPostPage() {
  const navi = useNavigate();
  const store = useUserStore();
  const [artists, setArtists] = useState<Artist[]>([]);
  const [selectedArtist, setSelectedArtist] = useState<string | undefined>(
    undefined,
  );
  const [selectedFriend, setSelectedFriend] = useState<Follow | null>(null);
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

  const selectMyself = () => {
    const myself: Follow = {
      id: store.userId,
      nickname: store.nickname,
      image: store.image,
      followingCount: store.followingCount, // 또는 적절한 기본값
      followerCount: store.followerCount, // 또는 적절한 기본값
      role: store.role, // 또는 'ROLE_USER' 등 적절한 기본값
    };
    setSelectedFriend(myself);
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
            image: part.image, // 파트의 이미지를 선택된 곡의 이미지로 업데이트
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
          'http://localhost:8081/api/covers/create',
          {
            method: 'POST',
            body: requestBody,
            // credentials: 'include',
            headers: {
              'Content-Type': 'application/json',
            },
          },
        );

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
    setSelectedFriend(defaultFollow);
  };

  const prevSlide = () => {
    const newIndex = (currentIndex - 1 + parts.length) % parts.length;
    setSelectedPart(parts[newIndex]); // 이전 슬라이드로 이동할 때 선택된 파트 업데이트
    setCurrentIndex(newIndex);
    setSelectedFriend(defaultFollow);
  };
  return (
    <div
      className={css.root}
      style={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'flex-start',
        height: '100%',
        marginTop: '8vh',
      }}
    >
      <div className={css.mainContent}>
        {/* 파트 선택 왼쪽 */}
        <div className={css.partList} style={{ margin: 'auto 0' }}>
          <ul
            className={css.partUl}
            style={{ width: '130%', marginLeft: '-20%' }}
          >
            {parts.map((part, index) => (
              <li style={{ margin: '10%' }} key={index} className={css.partLi}>
                <div className={css.partContainer}>
                  {/* Image and Name container */}
                  <div
                    className={css.partDiv}
                    style={{
                      alignItems: 'center',
                      justifyContent: 'center',
                      display: 'flex',
                      flexDirection: 'column',
                    }}
                  >
                    <img
                      src={part.image}
                      alt={part.name}
                      className={css.partImage}
                      style={{
                        width: '60px',
                        height: '60px',
                        borderRadius: '50%',
                      }} // Adjust width, height, and borderRadius as needed
                    />
                  </div>
                  {/* User Image and Name */}
                  <div style={{ marginLeft: '10%' }}>
                    <div>{part.name}</div>
                    {part.userImage && (
                      <div
                        className={css.profileContainer}
                        // style={{ display: 'none' }}
                      >
                        {' '}
                        {/* Hide user profile container */}
                        <div style={{ marginTop: '3%' }}>
                          <p>{part.userName}</p>
                        </div>
                      </div>
                    )}
                  </div>
                </div>
              </li>
            ))}
          </ul>
        </div>
        {/* 가운데 카로셀 */}
        <div className={css.carouselContainer} style={{ flexGrow: 2 }}>
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

          {/* 배경 및 이미지 컨테이너 */}
          <div
            className={css.carouselControls}
            style={{
              position: 'relative', // 부모 컨테이너에 대해 절대 위치 설정을 위함
              width: '100%',
              height: '300px',
              background: 'blur',
            }}
          >
            <button onClick={prevSlide} className={css.controlButton}>
              ◀
            </button>
            {/* 전 */}

            <div
              style={{
                backgroundImage: `url(${
                  selectedSong?.image || '../../../../../public/images/Logo.png'
                })`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                backgroundRepeat: 'no-repeat',
                width: '100%',
                height: '300px', // 조정 가능
                display: 'flex',
                alignItems: 'center',
                padding: '10%',
                // filter: 'blur(5px)', // 블러 효과
                // zIndex: -1, // 다른 컨텐츠 아래에 배치
                justifyContent: 'space-around', // 이 부분은 이미지들 사이의 간격을 조절합니다.
              }}
            >
              {/* 왼쪽 이미지 */}
              <img
                style={{ borderRadius: '50%', width: '100px', height: '100px' }}
                src={images[currentIndex]}
              />
              {/* 오른쪽 이미지 */}
              {selectedFriend && ( // selectedFriend가 있을 때만 이미지를 렌더링
                <img
                  style={{
                    borderRadius: '50%',
                    width: '100px',
                    height: '100px',
                  }}
                  src={selectedFriend.image} // 선택된 친구의 이미지 경로를 사용
                  alt="User Image"
                />
              )}
            </div>
            {/* 전 */}

            <button onClick={nextSlide} className={css.controlButton}>
              ▶
            </button>
          </div>

          <button
            onClick={handleUpload}
            className={css.uploadButton}
            style={{ textAlign: 'center', marginTop: '8%' }}
          >
            <img
              src="https://navi.s3.ap-northeast-2.amazonaws.com/%EB%A7%8C%EB%93%A4%EA%B8%B0-%EB%A7%A4%EC%B9%AD+%EB%B2%84%ED%8A%BC.png"
              alt=""
            />
          </button>
        </div>
        {/* 친구 고르기 오른쪽 */}
        {/* 친구 고르기 오른쪽 */}
        <div
          className={css.followListContainer}
          style={{ margin: 'auto 0', marginRight: '6%' }}
        >
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
              <div
                className={css.profilePicContainer}
                onClick={selectMyself} // "나"를 선택하는 경우
              >
                <img
                  src={store.image}
                  alt="내 프로필 사진"
                  className={css.profilePic}
                />
                {/* ... */}
              </div>
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
                    onClick={() => {
                      handleUserClick(follow);
                      setSelectedFriend(follow);
                    }}
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
      <button
        onClick={handleUpload}
        className={css.uploadButton}
        style={{ textAlign: 'center' }}
      >
        <img
          src="https://navi.s3.ap-northeast-2.amazonaws.com/%EB%A7%8C%EB%93%A4%EA%B8%B0-%EB%A7%A4%EC%B9%AD+%EB%B2%84%ED%8A%BC.png"
          alt=""
        />
      </button>
    </div>
  );
}
