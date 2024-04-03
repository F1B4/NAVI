import React, { useState, useRef } from 'react';
import ReactPlayer from 'react-player';
import css from './playbar.module.css';
// import { CoverDetailPage } from '@/pages/coverDetail';
// import { NoraebangDetailPage } from '@/pages/noraebangDetail';
import { usePlayStore } from '@/shared/store';

interface PlaybarProps {
  pk: number;
  type: string;
  url: string;
  title: string; // 곡 제목
  coverImage: string; // 곡 커버 이미지 URL
  artist: string;
}

const Playbar: React.FC<PlaybarProps> = ({
  pk,
  type,
  url,
  title,
  coverImage,
  artist,
}) => {
  const play = usePlayStore();
  const [playing, setPlaying] = useState<boolean>(true); // 재생 중 여부
  const [played, setPlayed] = useState<number>(0); // 현재 진행 상태
  const [volume, setVolume] = useState<number>(0.5); // 볼륨 상태
  const [muted, setMuted] = useState<boolean>(false); // 음소거 상태
  const [expanded, setExpanded] = useState<boolean>(true); // 디테일 펼치는지 아닌지
  const [duration, setDuration] = useState<number>(0); // 비디오의 총 길이를 저장하는 상태
  const playerRef = useRef<ReactPlayer>(null); // 리액트플레이어

  const togglePlay = (): void => {
    setPlaying(!playing);
  };

  const toggleExpand = (): void => {
    setExpanded(!expanded);
  };

  const toggleMute = (): void => {
    setMuted(!muted); // 음소거 토글
  };

  const onProgress = (state: { played: number }): void => {
    setPlayed(state.played);
  };

  const onDuration = (duration: number): void => {
    setDuration(duration);
  };

  // 현재 재생 시간을 분과 초로 변환하는 함수
  const formatTime = (seconds: number): string => {
    const result = new Date(seconds * 1000).toISOString().substr(14, 5);
    return result;
  };

  if (url !== '') {
    return (
      <div
        style={{
          position: 'fixed',
          bottom: 0,
          left: 0,
          right: 0,
          backgroundColor: '#020715',
          transition: 'height 0.5s ease', // 부드럽게 올라가는 효과
          height: expanded ? 'calc(100% - 75px)' : '80px', // 높이 조정 -> 퍼센트로 했는데 우째 조정해야할지 모르겟어요
          zIndex: 2000,
          display: 'flex',
          flexDirection: 'column',
        }}
      >
        {/* 전체 */}
        {/* 상단좌측 */}
        <div
          style={{
            display: expanded ? 'flex' : 'none',
            flexDirection: 'row',
            height: '100%',
            alignItems: 'center',
            marginLeft: expanded ? '320px' : '0px',
            zIndex: 100,
          }}
        >
          {type === 'cover' ? (
            <div
              onClick={togglePlay}
              style={{
                width: '100%',
              }}
            >
              <ReactPlayer
                ref={playerRef} // 리액트 플레이어 참조값
                url={url} // 비디오 주소
                playing={playing} // 재생하고있는지 아닌지 (지금은 검사용으로 false했는데 디테일 들어가면 true로 바꿔줘야함)
                onProgress={onProgress} // 재생바 컨트롤
                onDuration={onDuration} // 비디오의 총 길이를 얻기 위한 이벤트 핸들러
                volume={muted ? 0 : volume} // 볼륨 조절, 음소거 상태에 따라 볼륨 조절
                muted={muted} // 음소거 상태 전달
                width="100%" // 비디오의 너비
                height="100%" // 비디오의 높이
              />
            </div>
          ) : (
            <ReactPlayer
              ref={playerRef} // 리액트 플레이어 참조값
              url={url}
              playing={playing}
              onProgress={onProgress}
              onDuration={onDuration}
              volume={muted ? 0 : volume}
              muted={muted}
              width="100%"
              height="100%"
            />
          )}
          {/* 우측 */}

          {/* 여기 들어와야함 */}

          <div
            style={{
              display: expanded ? 'flex' : 'none',
              flexDirection: 'column',
              alignItems: 'center',
              zIndex: 200,
              width: '100%',
              height: '600px',
              border: 'solid white',
            }}
          >
            {type}
            {/* {type === 'cover' ? (
              <CoverDetailPage pk={play.pk} />
            ) : (
              <NoraebangDetailPage pk={play.pk} />
            )} */}

            {type === 'cover' ? '지랄하고' : '자빠졌네'}
          </div>
        </div>
        {/* 하단하단하단 */}
        <div>
          {/* 하단바 */}
          <div
            style={{
              position: 'absolute',
              backgroundColor: '#020715',
              width: '100%',
              left: 0,
              right: 0,
              bottom: '60px',
              paddingBottom: '10px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              zIndex: 9998,
            }}
          >
            {/* 진행 바 */}
            <input
              type="range"
              min="0"
              max="0.999999"
              step="any"
              value={played}
              onChange={(e) => {
                const value = parseFloat(e.target.value);
                setPlayed(value);
                playerRef.current?.seekTo(value, 'fraction');
              }}
              style={{ flexGrow: 1 }}
            />
          </div>
        </div>
        {/* 하단설명 */}
        <div
          style={{
            backgroundColor: '#020715',
            position: 'absolute',
            left: 0,
            right: 0,
            bottom: '0px',
            padding: '10px',
            display: 'flex',
            flexDirection: 'row',
            alignItems: 'center',
            justifyContent: 'center',
            zIndex: 9500,
          }}
        >
          {/* 처음 */}
          <div
            style={{
              display: 'flex',
              width: '100%',
              justifyContent: 'space-between',
              zIndex: 9998,
            }}
          >
            <div
              style={{
                display: 'flex',
                alignItems: 'center',
                margin: '0 20px',
                zIndex: 9998,
              }}
            >
              <button onClick={togglePlay}>{playing ? '⏸' : '▶'}</button>
              {/* 현재 재생 시간과 전체 시간을 표시 */}
              <span style={{ marginRight: '10px', marginLeft: '10px' }}>
                {formatTime(played * duration)} / {formatTime(duration)}
              </span>
            </div>
            {/* 중간 */}
            {/* 곡 정보 표시 */}

            <div
              style={{
                display: 'flex',
                alignItems: 'center',
                margin: '0 20px',
              }}
            >
              {coverImage && (
                <img
                  src={coverImage}
                  alt="Cover"
                  style={{
                    width: type === 'cover' ? '80px' : '50px',
                    height: '50px',
                    marginRight: '10px',
                  }}
                />
              )}
              <div className={css.desc}>
                {title && (
                  <span
                    style={{
                      marginRight: '10px',
                      marginBottom: '2px',
                      fontSize: '24px',
                    }}
                  >
                    {title}
                  </span>
                )}
                {artist && <span>{artist}</span>}
              </div>
            </div>
            {/* 끝 */}
            <div
              style={{
                display: 'flex',
                alignItems: 'center',
                margin: '0 20px',
              }}
            >
              {/* 음소거 */}
              <button onClick={toggleMute}>{muted ? '🔇' : '🔊'}</button>
              {/* 볼륨 조절 */}
              <input
                type="range"
                min="0"
                max="1"
                step="any"
                value={volume}
                onChange={(e) => {
                  const newVolume = parseFloat(e.target.value);
                  setVolume(newVolume);
                  if (muted && newVolume > 0) {
                    setMuted(false); // 볼륨 조정시 음소거 해제
                  }
                }}
                style={{ width: '100px', marginRight: '10px' }}
              />
              {/* 재생바 펼치기/접기 */}
              <button onClick={toggleExpand}>{expanded ? '▼' : '▲'}</button>
            </div>
          </div>
        </div>
      </div>
    );
  }
  return null;
};

export default Playbar;
