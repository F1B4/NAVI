package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.navi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
