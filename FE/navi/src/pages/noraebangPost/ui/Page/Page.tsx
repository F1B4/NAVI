import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import css from './Page.module.css';
import { FILE } from 'dns';

// Artist, Song, Lyric 인터페이스 수정
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

interface Lyric {
  id: number;
  startTime: string;
  endTime: string;
  content: string;
  sequence: number;
  partDto: {
    id: number;
    image: string;
    name: string;
  };
}

export function NoraebangPostPage() {
  const [showTextBox, setShowTextBox] = useState<boolean>(false); // 텍스트 박스 보이기 여부를 나타내는 상태 추가
  const [isRecording, setIsRecording] = useState<boolean>(false); // isRecording 타입 지정
  const [recordedChunks, setRecordedChunks] = useState<Blob[]>([]);
  const [songPk] = useState<number | null>(5);
  const [content, setContent] = useState<string | null>('');
  const [audioUrl, setAudioUrl] = useState<string | null>(null);
  const mediaRecorderRef = useRef<MediaRecorder | null>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);
  const audioElementRef = useRef<HTMLAudioElement>(null);
  const [finalBlob, setFinalBlob] = useState<Blob | null>(null);
  const [artists, setArtists] = useState<Artist[]>([]);
  const [selectedArtist, setSelectedArtist] = useState<string | undefined>(
    undefined,
  );
  const [songs, setSongs] = useState<Song[]>([]);
  const [selectedSong, setSelectedSong] = useState<Song | undefined>(undefined);
  const [lyrics, setLyrics] = useState<string>('');

  const stopRecording = () => {
    if (
      mediaRecorderRef.current &&
      mediaRecorderRef.current.state === 'recording'
    ) {
      mediaRecorderRef.current.stop();
      setIsRecording(false);
    }
  };

  const handleUpload = async () => {
    if (!finalBlob) {
      console.error('No audio to upload');
      return;
    }
    try {
      const formData = new FormData();
      const file = new File([finalBlob], 'recorded_audio.webm', {
        type: 'audio/webm',
      });
      formData.append('file', file);
      formData.append('song_pk', String(songPk));
      formData.append('content', String(content));
      const response = await fetch(
        'http://localhost:8081/api/noraebangs/create',
        {
          method: 'POST',
          body: formData,
          credentials: 'include',
        },
      );

      if (!response.ok) {
        console.log(response);
        throw new Error('Failed to upload audio');
      }

      console.log('Audio uploaded successfully');
    } catch (error) {
      console.error('Error uploading audio:', error);
    }
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setRecordedChunks([file]);
    }
  };

  const startRecording = async () => {
    try {
      setRecordedChunks([]);
      setAudioUrl(null);

      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      const mediaRecorder = new MediaRecorder(stream, {
        mimeType: 'audio/webm',
      });
      mediaRecorderRef.current = mediaRecorder;

      mediaRecorder.ondataavailable = (e) => {
        if (e.data.size > 0) {
          setRecordedChunks((prevChunks) => [...prevChunks, e.data]);
          console.log(recordedChunks);
        }
      };
      mediaRecorder.onstop = () => {
        setRecordedChunks((prevChunks) => {
          const blob = new Blob(prevChunks, { type: 'audio/webm' });
          setFinalBlob(blob);
          const url = URL.createObjectURL(blob);
          setAudioUrl(url);
          return [];
        });
      };

      mediaRecorder.start();
      setIsRecording(true);
      setTimeout(() => {
        stopRecording();
      }, 300000);
    } catch (error) {
      console.error('Error accessing microphone:', error);
    }
  };

  useEffect(() => {
    const fetchArtists = async () => {
      try {
        const response = await axios.get<{
          resultCode: string;
          message: string;
          data: Artist[];
        }>('http://localhost:8081/api/noraebangs/info', {
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

    fetchArtists();
  }, []);

  const fetchSongsByArtist = async (artistPk: number) => {
    try {
      const response = await axios.get<{
        resultCode: string;
        message: string;
        data: Song[];
      }>(`http://localhost:8081/api/noraebangs/${artistPk}/songs`, {
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
          `http://localhost:8081/api/noraebangs/${songId}/lyrics`,
          {
            withCredentials: true,
          },
        );
        if (response.data.resultCode === 'OK') {
          const lyricsData = response.data.data;
          const lyricsText = lyricsData
            .map((lyric: Lyric) => lyric.content)
            .join('\n');
          setLyrics(lyricsText);
        } else {
          console.error('가사를 가져오는 중 오류 발생:', response.data.message);
        }
      } catch (error) {
        console.error('가사를 가져오는 중 오류 발생:', error);
      }
    } else {
      setLyrics('');
    }
  };

  return (
    <div
      style={{
        padding: '15px',
        textAlign: 'center',
        position: 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
      }}
    >
      <div style={{}}>
        {/* 가수랑 곡 선택 */}
        <div
          style={{
            margin: '0 auto',
            marginBottom: '20px',
            paddingLeft: '10px',
            width: '100%',
            display: 'flex',
          }}
        >
          <select
            className={css.dropdown}
            style={{
              margin: '0 0px',
              width: '90%',
            }}
            onChange={handleArtistChange}
            value={selectedArtist}
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

          <select
            className={css.dropdown}
            style={{ margin: '0 10px', width: '90%' }}
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
        </div>

        {/* 가사랑 사진 */}
        <div
          style={{
            position: 'relative',
            width: '610px',
            height: '280px',
            overflow: 'auto',
            /* 크롬 브라우저에서 스크롤바를 숨김 */
            WebkitOverflowScrolling:
              'touch' /* 터치 디바이스에서 스크롤 속도 조정 */,
            scrollbarWidth:
              'none' /* 모질라 기반 브라우저에서 스크롤바를 숨김 */,
            msOverflowStyle: 'none' /* IE에서 스크롤바를 숨김 */,
          }}
        >
          <div
            style={{
              backgroundImage: selectedSong
                ? `url(${selectedSong.image})`
                : 'none',
              backgroundSize: 'cover',
              backgroundPosition: 'center',
              filter: 'blur(5px)',
              width: '610px',
              height: '280px',
              position: 'fixed',
              objectFit: 'cover',
            }}
          ></div>
          <div
            style={{
              width: '610px',
              height: '280px',
              color: 'white',
              position: 'relative',
              textAlign: 'center',
            }}
          >
            {lyrics &&
              lyrics.split('\n').map((line, index) => (
                <div
                  style={{
                    backgroundColor: 'rgba(0, 0, 0, 0.5)',
                    zIndex: '3400',
                    position: 'relative',
                  }}
                  key={index}
                >
                  {line}
                </div>
              ))}
          </div>
        </div>
      </div>

      {/* 추가된 div */}
      <div>
        <input
          type="file"
          ref={fileInputRef}
          style={{ display: 'none' }}
          onChange={handleFileChange}
        />
        {/* 녹음이 중지된 후에 오디오를 재생할 수 있는 요소 추가 */}
        {audioUrl && (
          <div
            style={{
              marginTop: '20px',
              display: 'flex',
              justifyContent: 'center',
            }}
          >
            <audio style={{ width: '90%' }} ref={audioElementRef} controls>
              <source src={audioUrl} type="audio/wav" />
              Your browser does not support the audio element.
            </audio>
          </div>
        )}
        <div>
          {/*  */}
          {/*  */}

          <button
            onClick={isRecording ? stopRecording : startRecording}
            style={{
              marginTop: '20px',
              backgroundImage: `url('https://navi.s3.ap-northeast-2.amazonaws.com/recordButton.png')`,
              backgroundSize: 'cover',
              backgroundPosition: 'center',
              backgroundColor: 'white',
              borderRadius: '50%',
              width: '45px', // 이미지 크기에 맞게 조절하세요
              height: '45px', // 이미지 크기에 맞게 조절하세요
            }}
          >
            {isRecording}
          </button>
          {/* {showTextBox && <h2>게시하기</h2>} */}

          <div style={{ textAlign: 'right', position: 'relative' }}>
            {/* 텍스트 박스 */}
            {showTextBox && (
              <textarea
                style={{
                  width: '95%',
                  margin: '20px',
                  padding: '10px',
                  borderRadius: '5px',
                  border: '1px solid #ccc',
                  resize: 'none',
                  fontFamily: 'Arial, sans-serif',
                  fontSize: '14px',
                  lineHeight: '1.5',
                  backgroundColor: 'lightgray',
                  color: 'black', // 검은색 텍스트
                }}
                placeholder="텍스트를 입력하세요"
                value={content || ''}
                onChange={(e) => setContent(e.target.value)}
              />
            )}

            {!showTextBox && (
              <h2
                style={{
                  color: 'white',
                  marginRight: '10px',
                  fontSize: '13px',
                }}
              >
                게시유무
              </h2>
            )}

            {/* 체크박스 */}
            <input
              type="checkbox"
              checked={showTextBox}
              onChange={() => setShowTextBox(!showTextBox)}
              style={{
                position: 'absolute',
                top: '50%',
                transform: 'translateY(-50%)',
              }}
            />
          </div>
        </div>
        <button onClick={handleUpload}>Upload Audio</button>
      </div>
    </div>
  );
}
