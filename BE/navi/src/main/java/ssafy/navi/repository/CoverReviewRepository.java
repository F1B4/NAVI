package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.cover.CoverReview;

@Repository
public interface CoverReviewRepository extends JpaRepository<CoverReview,Long> {

}
