import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios, { AxiosResponse } from 'axios';
import { Card } from '@/shared/ui';
import { baseApi } from '@/shared/api';
import type { SongInfo } from '@/entities/noraebangList/api/types';
import type { Response } from '@/pages/searchResults/ui/Page/types';
import css from '../../../searchResults/ui/Page/Page.module.css';

interface SearchResult {
  id: number;
  video?: string;
  record?: string;
  thumbnail: string;
  userDto?: { nickname: string };
  songDto: SongInfo; // songDto의 타입을 정확히 알 수 없는 경우 any로 설정
}

export function SearchResultsDetailPage(): JSX.Element {
  const { keyword, type } = useParams<{ keyword: string; type: string }>();
  const [searchResults, setSearchResults] = useState<SearchResult[]>([]);
  const [load, setLoad] = useState<boolean>(false);

  useEffect(() => {
    const getSearchResults = async () => {
      try {
        let apiEndpoint = '';
        // type에 따라 API 엔드포인트 설정
        switch (type) {
          case 'cover-title':
            apiEndpoint = `${baseApi}/main/cover/title/${keyword}`;
            break;
          case 'cover-artist':
            apiEndpoint = `${baseApi}/main/cover/artist/${keyword}`;
            break;
          case 'noraebang-title':
            apiEndpoint = `${baseApi}/main/noraebang/title/${keyword}`;
            break;
          case 'noraebang-artist':
            apiEndpoint = `${baseApi}/main/noraebang/artist/${keyword}`;
            break;
          case 'user':
            apiEndpoint = `${baseApi}/main/user/${keyword}`;
            break;
          default:
            throw new Error('Invalid search type');
        }
        const response: AxiosResponse<Response> = await axios.get(apiEndpoint);

        // 결과 설정
        setSearchResults(response.data.data);
        setLoad(true);
      } catch (error) {
        console.error('Error fetching search results:', error);
      }
    };
    getSearchResults();
  }, []);

  return (
    <div className={css.root}>
      <div className={css.title}>&apos;{keyword}&apos; 검색 결과</div>
      {load ? (
        <div>
          {searchResults.length === 0 ? (
            <div>검색 결과가 없습니다.</div>
          ) : (
            <div className={css.container}>
              {type === 'cover-title' && (
                <div className={css.subtitle}>AI 커버 제목</div>
              )}
              {type === 'noraebang-title' && (
                <div className={css.subtitle}>노래방 제목</div>
              )}
              {type === 'noraebang-artist' && (
                <div className={css.subtitle}>노래방 가수</div>
              )}
              {type === 'cover-artist' && (
                <div className={css.subtitle}>AI 커버 가수</div>
              )}
              {searchResults.map((result, index) => (
                <Card
                  video={result.video}
                  record={result.record}
                  id={result.id}
                  key={index}
                  classCard={css.card}
                  classImg={
                    type === 'noraebang/title' ? css.noraebangImg : css.coverImg
                  }
                  classDesc={
                    type === 'noraebang/title'
                      ? css.noraebangDesc
                      : css.coverDesc
                  }
                  thumbnail={result.thumbnail}
                  user={result.userDto?.nickname || ''}
                  type={type === 'cover' ? 'cover' : 'noraebang'}
                  info={result.songDto}
                />
              ))}
            </div>
          )}
        </div>
      ) : null}
    </div>
  );
}
