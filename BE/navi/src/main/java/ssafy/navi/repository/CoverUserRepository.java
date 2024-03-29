package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.navi.entity.cover.CoverUser;

import java.util.List;

public interface CoverUserRepository extends JpaRepository<CoverUser,Long> {
    List<CoverUser> findByUserId(Long userPk);
}
