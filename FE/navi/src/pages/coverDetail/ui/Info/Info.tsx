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
      <div style={{ margin: '10%', marginLeft: '0%' }}>
        <h1>커버 정보</h1>
      </div>
      <div
        className={css.songInfo}
        style={{
          display: 'flex',
          marginRight: '20%',
          justifyContent: 'start',
        }}
      >
        <div className={css.img}>
          <img src={props.image} alt="" />
        </div>
        <div
          className={css.title}
          style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
          }}
        >
          <h2 style={{ marginLeft: '30px' }}>{props.title}</h2>
        </div>
      </div>
      <div className={css.userInfo}>
        {Array.isArray(props.singers) &&
          props.singers.map((singer, index) => (
            <div key={index}>
              <div style={{ display: 'flex' }}>
                <div style={{ display: 'flex' }}>
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
          ))}
      </div>
    </div>
  );
}
