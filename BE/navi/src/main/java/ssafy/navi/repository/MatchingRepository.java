package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.dto.MatchingDto;
import ssafy.navi.entity.Matching;

import java.util.List;

@Repository
public interface MatchingRepository extends JpaRepository<Matching,Long> {
    List<Matching> findBySongId(Long songPk);
}
