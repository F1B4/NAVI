package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.navi.entity.Cover;

import java.util.List;

public interface CoverRepository extends JpaRepository<Cover,Long> {
    public List<Cover> findById(Long coverPk);
    public Cover findbyId(Long coverPk);
}
