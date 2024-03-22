package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.song.Song;

@Repository
public interface SongRepository extends JpaRepository<Song, Long>{
}
