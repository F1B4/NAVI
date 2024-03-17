package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.NoraebangLike;

import java.util.Optional;

@Repository
public interface NoraebangLikeRepository extends JpaRepository<NoraebangLike, Long> {
    Optional<NoraebangLike> findByNoraebangIdAndUserId(Long NoraebangId, Long userId);
}
