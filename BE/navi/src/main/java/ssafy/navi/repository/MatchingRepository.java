package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.cover.Matching;

import java.util.List;

@Repository
public interface MatchingRepository extends JpaRepository<Matching,Long> {
    List<Matching> findBySongId(Long songPk);
}
