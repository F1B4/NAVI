package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.Noraebang;

import java.util.List;

@Repository
public interface NoraebangRepository extends JpaRepository<Noraebang, Long> {
    @Query("SELECT n FROM Noraebang n WHERE n.song.title LIKE %:keyword% ORDER BY n.createdAt DESC")
    List<Noraebang> findTop3BySongTitleContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);
    @Query("SELECT n FROM Noraebang n WHERE n.song.artist.name LIKE %:keyword% ORDER BY n.createdAt DESC LIMIT 3")
    List<Noraebang> findTop3ByArtistNameContainingOrderByCreatedAtDesc(@Param("kyeword") String keyword);
    List<Noraebang> findBySongTitleContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);
    @Query("SELECT n FROM Noraebang n WHERE n.song.artist.name LIKE %:keyword% ORDER BY n.createdAt DESC")
    List<Noraebang> findByArtistNameContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);

}
