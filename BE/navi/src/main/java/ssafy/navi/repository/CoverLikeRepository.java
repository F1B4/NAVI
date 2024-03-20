package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.Cover;
import ssafy.navi.entity.CoverLike;
import ssafy.navi.entity.User;

import java.util.Optional;

@Repository
public interface CoverLikeRepository extends JpaRepository<CoverLike,Long> {
    Optional<CoverLike> findByCoverAndUser(Cover cover, User user);

}
