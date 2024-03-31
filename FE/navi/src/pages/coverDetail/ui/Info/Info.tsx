import css from './Info.module.css';

interface InfoProps {
  title: string;
  singer: string;
}

export function Info(props: InfoProps) {
  return (
    <div className={css.root}>
      <h1>커버 정보</h1>
      <div className={css.songInfo}>
        <div className={css.title}>{props.title}</div>
        <div className={css.singer}>{props.singer}</div>
      </div>
      <div className={css.userInfo}>
        <div>
          v-for 가수들
          <div>가수</div>
          <div>유저</div>
        </div>
      </div>
    </div>
  );
}
