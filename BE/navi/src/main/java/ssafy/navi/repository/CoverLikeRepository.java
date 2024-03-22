package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.cover.Cover;
import ssafy.navi.entity.cover.CoverLike;
import ssafy.navi.entity.user.User;

import java.util.Optional;

@Repository
public interface CoverLikeRepository extends JpaRepository<CoverLike,Long> {
    Optional<CoverLike> findByCoverAndUser(Cover cover, User user);

}
