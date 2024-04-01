package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.cover.Cover;
import ssafy.navi.entity.noraebang.Noraebang;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoraebangRepository extends JpaRepository<Noraebang, Long> {

    // TopN을 사용하면 N만큼의 결과만을 조회함
    List<Noraebang> findTop10ByCreatedAtAfterOrderByHitDesc(LocalDateTime startDate);
    List<Noraebang> findTop6ByCreatedAtAfterOrderByWeeklyHitDesc(LocalDateTime startDate);

    @Query(value = "SELECT COUNT(*) FROM noraebang WHERE user_pk = :userId", nativeQuery = true)
    Integer countByUserId(@Param("userId") Long userId);

    //노래방 제목으로 조회 3개
    @Query("SELECT n FROM Noraebang n WHERE n.song.title LIKE %:keyword% ORDER BY n.createdAt DESC LIMIT 3")
    List<Noraebang> findTop3BySongTitleContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);

    //노래방 원곡자 이름으로 조회 3개
    @Query("SELECT n FROM Noraebang n WHERE n.song.artist.name LIKE %:keyword% ORDER BY n.createdAt DESC LIMIT 3")
    List<Noraebang> findTop3ByArtistNameContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);

    //원곡제목으로 검색
    List<Noraebang> findBySongTitleContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);
    @Query("SELECT n FROM Noraebang n WHERE n.song.artist.name LIKE %:keyword% ORDER BY n.createdAt DESC")
    List<Noraebang> findByArtistNameContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);

    List<Noraebang> findTop10ByOrderByCreatedAtDesc();
}
