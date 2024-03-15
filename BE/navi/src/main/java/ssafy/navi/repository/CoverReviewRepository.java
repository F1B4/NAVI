package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.CoverReview;

import java.util.List;

@Repository
public interface CoverReviewRepository extends JpaRepository<CoverReview,Long> {

}
