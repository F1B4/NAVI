import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import css from './Searchbar.module.css';

export function Searchbar() {
  const [keyword, setKeyword] = useState('');
  const navigate = useNavigate();
  const [isFocused, setIsFocused] = useState(false); // 입력란에 포커스 여부를 저장하는 상태

  const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      navigate(`/search/${keyword}`);
      setKeyword('');
    }
  };

  const handleFocus = () => {
    setIsFocused(true);
  };

  const handleBlur = () => {
    setIsFocused(false);
  };

  return (
    <div className={css.root}>
      <input
        type="text"
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
        onKeyPress={handleKeyPress}
        onFocus={handleFocus}
        onBlur={handleBlur}
        placeholder={isFocused ? '' : '검색어를 입력하세요'}
        className={css.inputField}
      />
    </div>
  );
}
