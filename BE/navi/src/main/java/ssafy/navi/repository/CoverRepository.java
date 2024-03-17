package ssafy.navi.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.Cover;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoverRepository extends JpaRepository<Cover,Long> {
    // TopN을 사용하면 N만큼의 결과만을 조회함
    List<Cover> findTop6ByCreatedAtAfterOrderByHitDesc(LocalDateTime startDate);
}
