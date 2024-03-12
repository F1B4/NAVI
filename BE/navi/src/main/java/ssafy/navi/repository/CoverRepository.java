package ssafy.navi.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.Cover;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoverRepository extends JpaRepository<Cover,Long> {

}
