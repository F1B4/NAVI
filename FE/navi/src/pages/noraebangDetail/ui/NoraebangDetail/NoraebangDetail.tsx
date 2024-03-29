import css from './NoraebangDetail.module.css';

interface NoraebangProps {
  image: string;
}

export function NoraebangDetail(props: NoraebangProps) {
  const rootStyle = {
    backgroundImage: `url(${props.image})`,
  };

  return (
    <div className={css.root} style={rootStyle}>
      <h1>가사</h1>
    </div>
  );
}
