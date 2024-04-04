import type { CoverInfo } from '@/entities/coverDetail/api/types';
import css from './Info.module.css';

interface InfoProps {
  title: string;
  image: string;
  singers: CoverInfo[];
}

export function Info(props: InfoProps) {
  return (
    <div className={css.root}>
      <div
        className={css.songInfo}
        style={{
          display: 'flex',
          marginRight: '20%',
          justifyContent: 'start',
        }}
      >
        {/* 곡 커버 사진 */}
        <div className={css.img}>
          <img src={props.image} alt="" />
        </div>
        {/* 곡 제목 */}
        <div
          className={css.title}
          style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
          }}
        >
          <h1 style={{ marginLeft: '30px' }}>{props.title}</h1>
        </div>
      </div>
      <div className={css.userInfo}>
        {Array.isArray(props.singers) &&
          props.singers.map((singer, index) => (
            <div key={index}>
              <div className={css.set}>
                {/* 가수 정보 */}
                <div
                  style={{
                    display: 'flex',
                    marginRight: '100px',
                  }}
                >
                  {/* 가수이미지 */}
                  <div
                    className={css.img}
                    style={{
                      display: 'flex',
                      justifyContent: 'center',
                      alignItems: 'center',
                    }}
                  >
                    <img
                      style={{
                        width: '60%',
                        height: '60%',
                        borderRadius: '50%',
                        objectFit: 'cover',
                      }}
                      src={singer.partDto.image}
                      alt=""
                    />
                  </div>
                  {/* 가수 이름 */}
                  <div
                    style={{
                      marginLeft: '-8%',
                      display: 'flex',
                      justifyContent: 'center',
                      alignItems: 'center',
                    }}
                  >
                    <p>{singer.partDto.name}</p>
                  </div>
                </div>

                {/* 유저 정보 */}
                <div>
                  <div
                    style={{
                      display: 'flex',
                      justifyContent: 'center',
                      alignItems: 'center',
                    }}
                  >
                    <div
                      className={css.img}
                      style={{
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'center',
                      }}
                    >
                      <img
                        style={{
                          width: '60%',
                          height: '60%',
                          borderRadius: '50%',
                          objectFit: 'cover',
                        }}
                        src={singer.userDto.image}
                        alt=""
                      />
                    </div>
                    <div style={{ marginLeft: '-8%' }}>
                      <p>{singer.userDto.nickname}</p>
                    </div>
                  </div>
                </div>
              </div>

              {/* 끝 */}
            </div>
          ))}
      </div>
    </div>
  );
}
