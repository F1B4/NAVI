package ssafy.navi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.noraebang.NoraebangReview;

@Repository
public interface NoraebangReviewRepository extends JpaRepository<NoraebangReview, Long> {
}
