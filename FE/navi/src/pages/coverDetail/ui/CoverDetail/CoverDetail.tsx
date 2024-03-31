import css from './CoverDetail.module.css';

interface CoverProps {
  video: string;
}

export function CoverDetail(props: CoverProps) {
  return (
    <div className={css.root}>
      <video src={props.video}></video>;
    </div>
  );
}
