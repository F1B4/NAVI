package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.cover.MatchingUser;
import ssafy.navi.entity.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchingUserRepository extends JpaRepository<MatchingUser,Long> {
    Optional<MatchingUser> findByUserIdAndPartId(Long userId, Long partId);
    List<MatchingUser> findAllByUser(User user);
}
