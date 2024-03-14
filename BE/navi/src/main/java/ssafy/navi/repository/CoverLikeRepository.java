package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.CoverLike;

@Repository
public interface CoverLikeRepository extends JpaRepository<CoverLike,Long> {
}
