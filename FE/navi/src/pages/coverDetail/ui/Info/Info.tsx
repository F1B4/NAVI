import css from './Info.module.css';

interface InfoProps {}

export function Info(props: InfoProps) {
  return (
    <div className={css.root}>
      <h1>커버 정보</h1>
      <div className={css.songInfo}>
        <div className={css.title}></div>
        <div className={css.singer}></div>
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
