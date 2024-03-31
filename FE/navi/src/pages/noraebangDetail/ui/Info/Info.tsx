import css from './Info.module.css';

interface InfoProps {
  title: string;
  user: string;
  content: string;
}

export function Info(props: InfoProps) {
  return (
    <div className={css.root}>
      <div className={css.title}>{props.title}</div>
      <div className={css.user}>{props.user}</div>
      <div className={css.content}>{props.content}</div>
    </div>
  );
}
