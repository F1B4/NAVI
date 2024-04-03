import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios, { AxiosResponse } from 'axios';
import { Card } from '@/shared/ui';
import { baseApi } from '@/shared/api';
import type {
  CoverUser,
  CoverList,
  NoraebangList,
  Response,
  User,
} from './types';
import css from './Page.module.css';

export function SearchResultsPage() {
  const { keyword } = useParams(); // URL의 키워드를 가져오기

  // API 호출을 위한 상태 설정
  const [coverResults, setCoverResults] = useState<CoverList>();
  const [coverUserResults, setCoverUserResults] = useState<CoverList>();
  const [noraebangResults, setNoraebangResults] = useState<NoraebangList>();
  const [noraebangUserResults, setNoraebangUserResults] =
    useState<NoraebangList>();
  const [userResults, setUserResults] = useState<User[]>([]);
  const [load, setLoad] = useState(false);

  // 검색 결과를 가져오는 함수
  const getSearchResults = async () => {
    try {
      // API 호출
      const response: AxiosResponse<Response> = await axios.get(
        `${baseApi}/main?keyword=${keyword}`,
      );
      // 결과 설정
      setCoverResults(response.data.data.cover);
      setCoverUserResults(response.data.data.coverArtist);
      setNoraebangResults(response.data.data.noraebang);
      setNoraebangUserResults(response.data.data.noraebangArtist);
      setUserResults(response.data.data.user);
      setLoad(true);
      console.log(coverResults);
      console.log(userResults);
      console.log(response.data.data.cover);
      console.log(response);
    } catch (error) {
      console.error('Error fetching search results:', error);
    }
  };

  const handleLoadMore = (keyword: string, type: string) => {
    // URL 생성
    let url = '';
    switch (type) {
      case 'cover-title':
        url = `/search/cover-title/${keyword}`;
        break;
      case 'cover-artist':
        url = `/search/cover-artist/${keyword}`;
        break;
      case 'noraebang-title':
        url = `/search/noraebang-title/${keyword}`;
        break;
      case 'noraebang-artist':
        url = `/search/noraebang-artist/${keyword}`;
        break;
      case 'user':
        url = `/search/user/${keyword}`;
        break;
      default:
        throw new Error('Invalid search type');
    }
    // 페이지 이동
    window.location.href = url;
  };

  useEffect(() => {
    getSearchResults();
  }, [keyword]);

  if (load) {
    return (
      <div className={css.root}>
        <div className={css.title}>&apos;{keyword}&apos; 검색 결과</div>
        {!coverResults &&
        !coverUserResults &&
        !noraebangResults &&
        !noraebangUserResults &&
        userResults.length === 0 ? (
          <div>검색 결과가 없습니다.</div>
        ) : (
          <div>
            {Array.isArray(coverResults) && coverResults.length !== 0 && (
              <div className={css.container}>
                <div className={css.subtitle}>AI 커버 제목</div>
                {coverResults.map((cover, index) => {
                  const coverUserNicknames = cover.coverUserDtos
                    ? Array.from(
                        new Set(
                          cover.coverUserDtos.map(
                            (coverUserDto: CoverUser) =>
                              coverUserDto.userDto.nickname,
                          ),
                        ),
                      ).join(', ')
                    : '';

                  return (
                    <Card
                      video={cover.video}
                      id={cover.id}
                      key={index}
                      classCard={css.card}
                      classImg={css.coverImg}
                      classDesc={css.coverDesc}
                      thumbnail={cover.thumbnail}
                      user={coverUserNicknames}
                      type="cover"
                      info={cover.songDto}
                    />
                  );
                })}
                {keyword && (
                  <button
                    onClick={() => handleLoadMore(keyword, 'cover-title')}
                    className={css.btn}
                  >
                    더보기
                  </button>
                )}
              </div>
            )}

            <div>
              {Array.isArray(coverUserResults) &&
                coverUserResults.length > 0 && (
                  <div className={css.container}>
                    <div className={css.subtitle}>AI 커버 가수</div>
                    {coverUserResults.map((cover, index) => {
                      const coverUserNicknames = cover.coverUserDtos
                        ? Array.from(
                            new Set(
                              cover.coverUserDtos.map(
                                (coverUserDto: CoverUser) =>
                                  coverUserDto.userDto.nickname,
                              ),
                            ),
                          ).join(', ')
                        : '';

                      return (
                        <Card
                          video={cover.video}
                          id={cover.id}
                          key={index}
                          classCard={css.card}
                          classImg={css.coverImg}
                          classDesc={css.coverDesc}
                          thumbnail={cover.thumbnail}
                          user={coverUserNicknames}
                          type="cover"
                          info={cover.songDto}
                        />
                      );
                    })}
                    {keyword && (
                      <button
                        onClick={() => handleLoadMore(keyword, 'cover-artist')}
                        className={css.btn}
                      >
                        더보기
                      </button>
                    )}
                  </div>
                )}
            </div>

            <div>
              {Array.isArray(noraebangResults) &&
                noraebangResults.length > 0 && (
                  <div className={css.container}>
                    <div className={css.subtitle}>노래방 제목</div>
                    {noraebangResults.map((noraebang, index) => {
                      return (
                        <Card
                          record={noraebang.record}
                          id={noraebang.id}
                          key={index}
                          classCard={css.card}
                          classImg={css.noraebangImg}
                          classDesc={css.noraebangDesc}
                          user={noraebang.userDto.nickname}
                          type="noraebang"
                          info={noraebang.songDto}
                        />
                      );
                    })}
                    {keyword && (
                      <button
                        onClick={() =>
                          handleLoadMore(keyword, 'noraebang-title')
                        }
                        className={css.btn}
                      >
                        더보기
                      </button>
                    )}
                  </div>
                )}
            </div>

            <div>
              {Array.isArray(noraebangUserResults) &&
                noraebangUserResults.length > 0 && (
                  <div className={css.container}>
                    <div className={css.subtitle}>노래방 가수</div>
                    {noraebangUserResults.map((noraebang, index) => {
                      return (
                        <Card
                          record={noraebang.record}
                          id={noraebang.id}
                          key={index}
                          classCard={css.card}
                          classImg={css.noraebangImg}
                          classDesc={css.noraebangDesc}
                          user={noraebang.userDto.nickname}
                          type="noraebang"
                          info={noraebang.songDto}
                        />
                      );
                    })}
                    {keyword && (
                      <button
                        onClick={() =>
                          handleLoadMore(keyword, 'noraebang-artist')
                        }
                        className={css.btn}
                      >
                        더보기
                      </button>
                    )}
                  </div>
                )}
            </div>

            <div>
              {Array.isArray(userResults) && userResults.length > 0 && (
                <div className={css.container}>
                  <div className={css.subtitle}>유저 이름</div>

                  {userResults.map((user, index) => (
                    <div key={index} className={css.userCard}>
                      <Link to={`/profile/${user.id}`}>
                        <div className={css.userImg}>
                          <img src={user.image} alt="" />
                        </div>
                      </Link>
                      <Link to={`/profile/${user.id}`}>
                        <span className={css.userDesc}>{user.nickname}</span>
                      </Link>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    );
  }
  return null;
}
