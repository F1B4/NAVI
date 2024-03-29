import css from './Info.module.css';

interface InfoProps {
  title: string | null;
  user: string | null;
  content: string | null;
}

export function Info(props: InfoProps) {
  return (
    <div className={css.root}>
      <div>{props.title}</div>
      <div>{props.user}</div>
      <div>{props.content}</div>
    </div>
  );
}
