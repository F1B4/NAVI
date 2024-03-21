package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.cover.MatchingUser;

@Repository
public interface MatchingUserRepository extends JpaRepository<MatchingUser,Long> {
}
