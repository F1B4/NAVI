import React, { useState, useRef } from 'react';
import ReactPlayer from 'react-player';

interface PlaybarProps {
  url: string;
}

const Playbar: React.FC<PlaybarProps> = ({ url }) => {
  const [playing, setPlaying] = useState<boolean>(false);
  const [played, setPlayed] = useState<number>(0);
  const [volume, setVolume] = useState<number>(0.5);
  const [expanded, setExpanded] = useState<boolean>(false);
  const playerRef = useRef<ReactPlayer>(null);

  const togglePlay = (): void => {
    setPlaying(!playing);
  };

  const toggleExpand = (): void => {
    setExpanded(!expanded);
  };

  const onProgress = (state: { played: number }): void => {
    setPlayed(state.played);
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
        height: expanded ? '90%' : '50px', // 높이 조정 -> 퍼센트로 했는데 우째 조정해야할지 모르겟어요
        zIndex: 10,
        display: 'flex',
        flexDirection: 'column',
      }}
    >
      <div
        style={{
          display: expanded ? 'flex' : 'none',
          flexDirection: 'row',
          height: '100%',
          alignItems: 'center',
        }}
      >
        <ReactPlayer
          ref={playerRef} // 무슨 관리하는거?라고했는데 그냥 냅두새우
          url={url} // 비디오 주소
          playing={playing} // 재생하고있는지 아닌지 (지금은 검사용으로 false했는데 디테일 들어가면 true로 바꿔줘야할듯)
          onProgress={onProgress} // 재생바 컨트롤하는 부분인듯??
          volume={volume} // 볼륨 조절하는 부분
          width="70%" // 비디오의 너비
          height="70%" // 비디오의 높이
        />
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
        <button onClick={togglePlay}>{playing ? '||' : '►'}</button>
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
          style={{ flexGrow: 1, margin: '0 20px' }}
        />
        <input
          type="range"
          min="0"
          max="1"
          step="any"
          value={volume}
          onChange={(e) => setVolume(parseFloat(e.target.value))}
          style={{ width: '100px' }}
        />
        <button onClick={toggleExpand}>{expanded ? '▼' : '▲'}</button>
      </div>
    </div>
  );
};

export default Playbar;
