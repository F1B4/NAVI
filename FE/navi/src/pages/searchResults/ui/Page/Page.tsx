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

  useEffect(() => {
    getSearchResults();
  }, [keyword]);

  return (
    <div className={css.root}>
      <div>'{keyword}' 검색 결과</div>
      {/* 로딩 중인지 확인하여 로딩 중일 경우 로딩 표시 */}
      {!load ? (
        <div className={css.center}>
          <span className="loading loading-spinner loading-lg"></span>
        </div>
      ) : (
        <div>
          <div className={css.subtitle}>AI 커버 제목</div>
          <div>
            {Array.isArray(coverResults) && coverResults.length === 0 ? (
              <div className={css.center}>검색 결과가 없습니다</div>
            ) : (
              <div className={css.container}>
                {Array.isArray(coverResults) ? (
                  coverResults.map((cover, index) => {
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
                  })
                ) : (
                  <div className={css.center}>
                    <span className="loading loading-spinner loading-lg"></span>
                  </div>
                )}
              </div>
            )}
          </div>

          <div className={css.subtitle}>AI 커버 가수</div>
          <div>
            {Array.isArray(coverUserResults) &&
            coverUserResults.length === 0 ? (
              <div className={css.center}>검색 결과가 없습니다</div>
            ) : (
              <div className={css.container}>
                {Array.isArray(coverUserResults) ? (
                  coverUserResults.map((cover, index) => {
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
                  })
                ) : (
                  <div className={css.center}>
                    <span className="loading loading-spinner loading-lg"></span>
                  </div>
                )}
              </div>
            )}
          </div>
          <div>
            <div className={css.subtitle}>노래방 제목</div>
            <div>
              {Array.isArray(noraebangResults) &&
              noraebangResults.length === 0 ? (
                <div className={css.center}>검색 결과가 없습니다</div>
              ) : (
                <div>
                  {Array.isArray(noraebangResults) ? (
                    noraebangResults.map((noraebang, index) => (
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
                    ))
                  ) : (
                    <div className={css.center}>
                      <span className="loading loading-spinner loading-lg"></span>
                    </div>
                  )}
                </div>
              )}
            </div>
            <div className={css.subtitle}>노래방 가수</div>
            <div className={css.container}>
              {Array.isArray(noraebangUserResults) &&
              noraebangUserResults.length === 0 ? (
                <div className={css.center}>검색 결과가 없습니다</div>
              ) : (
                <div>
                  {Array.isArray(noraebangUserResults) ? (
                    noraebangUserResults.map((noraebang, index) => (
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
                    ))
                  ) : (
                    <div className={css.center}>
                      <span className="loading loading-spinner loading-lg"></span>
                    </div>
                  )}
                </div>
              )}
            </div>

            <div className={css.subtitle}>유저 이름</div>
            <div className={css.container}>
              {Array.isArray(userResults) && userResults.length === 0 ? (
                <div className={css.center}>검색 결과가 없습니다</div>
              ) : (
                <div>
                  {Array.isArray(userResults) ? (
                    userResults.map((user, index) => (
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
                    ))
                  ) : (
                    <div className={css.center}>
                      <span className="loading loading-spinner loading-lg"></span>
                    </div>
                  )}
                </div>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
