package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.Noraebang;

import java.util.List;

@Repository
public interface NoraebangRepository extends JpaRepository<Noraebang, Long> {

    //노래방 제목으로 조회 3개
    @Query("SELECT n FROM Noraebang n WHERE n.song.title LIKE %:keyword% ORDER BY n.createdAt DESC")
    List<Noraebang> findTop3BySongTitleContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);

    //노래방 원곡자 이름으로 조회 3개
    @Query("SELECT n FROM Noraebang n WHERE n.song.artist.name LIKE %:keyword% ORDER BY n.createdAt DESC")
    List<Noraebang> findTop3ByArtistNameContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);

    //원곡제목으로 검색
    List<Noraebang> findBySongTitleContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);
    @Query("SELECT n FROM Noraebang n WHERE n.song.artist.name LIKE %:keyword% ORDER BY n.createdAt DESC")
    List<Noraebang> findByArtistNameContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);

}
