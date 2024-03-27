package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    Optional<User> findById(Long userPk);

    List<User> findByNicknameContainingOrderByNickname(String keyword);

    List<User> findTop3ByNicknameContainingOrderByNickname(String keyword);
}
