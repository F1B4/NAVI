package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.Noraebang;

@Repository
public interface NoraebangRepository extends JpaRepository<Noraebang, Long> {

}
