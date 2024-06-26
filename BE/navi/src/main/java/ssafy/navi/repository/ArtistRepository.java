package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.song.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

}
