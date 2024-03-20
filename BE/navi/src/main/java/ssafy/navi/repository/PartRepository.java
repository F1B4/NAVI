package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.Part;

import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<Part,Long> {
    List<Part> findBySongId(Long SongPk);
}
