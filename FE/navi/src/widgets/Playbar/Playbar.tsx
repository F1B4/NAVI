import React, { useState, useRef } from 'react';
import ReactPlayer from 'react-player';

interface PlaybarProps {
  url: string;
}

const Playbar: React.FC<PlaybarProps> = ({ url }) => {
  const [playing, setPlaying] = useState<boolean>(false); // 재생 중 여부
  const [played, setPlayed] = useState<number>(0); // 현재 진행 상태
  const [volume, setVolume] = useState<number>(0.5); // 볼륨 상태
  const [muted, setMuted] = useState<boolean>(false); // 음소거 상태
  const [expanded, setExpanded] = useState<boolean>(false); // 디테일 펼치는지 아닌지
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

  return (
    <div
      style={{
        position: 'fixed',
        bottom: 0,
        left: 0,
        right: 0,
        backgroundColor: '#020715',
        transition: 'height 0.5s ease', // 부드럽게 올라가는 효과
        height: expanded ? '100%' : '75px', // 높이 조정 -> 퍼센트로 했는데 우째 조정해야할지 모르겟어요
        zIndex: 10,
        display: 'flex',
        flexDirection: 'column',
      }}
    >
      {/* 전체 */}
      <div
        style={{
          display: expanded ? 'flex' : 'none',
          flexDirection: 'row',
          height: '100%',
          alignItems: 'center',
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
          width="70%" // 비디오의 너비
          height="70%" // 비디오의 높이
        />
        {/* 우측 */}
        <div
          style={{
            width: '30%',
            padding: '20px',
            display: expanded ? 'block' : 'none',
          }}
        >
          <h2>커버정보입니다우</h2>
          <p>이노래는익바오와황금윤기나는종이의우리의꿈AI커버입니다우</p>
        </div>
      </div>
      {/* 하단 */}
      <div
        style={{
          position: 'absolute',
          left: 0,
          right: 0,
          bottom: 0,
          padding: '10px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
        }}
      >
        <button onClick={togglePlay}>{playing ? '⏸' : '▶'}</button>
        <div
          style={{
            display: 'flex',
            alignItems: 'center',
            flexGrow: 1,
            margin: '0 20px',
          }}
        >
          {/* 현재 재생 시간과 전체 시간을 표시 */}
          <span style={{ marginRight: '10px' }}>
            {formatTime(played * duration)} / {formatTime(duration)}
          </span>
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
          style={{ width: '100px' }}
        />
        {/* 재생바 펼치기/접기 */}
        <button onClick={toggleExpand}>{expanded ? '▼' : '▲'}</button>
      </div>
    </div>
  );
};

export default Playbar;
