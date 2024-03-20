package ssafy.navi.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ssafy.navi.entity.Cover;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoverRepository extends JpaRepository<Cover,Long> {
    // TopN을 사용하면 N만큼의 결과만을 조회함
    List<Cover> findTop6ByCreatedAtAfterOrderByWeeklyHitDesc(LocalDateTime startDate);

    // Title을 like keyword검색을 통해 최신순으로 3개만 가져옴
    List<Cover> findTop3ByTitleContainingOrderByCreatedAtDesc(String keyword);

    List<Cover> findByTitleContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);

    @Query("SELECT c FROM Cover c JOIN c.song s JOIN s.artist a WHERE a.name LIKE %:keyword% ORDER BY c.createdAt DESC")
    List<Cover> findByArtistNameContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);
    @Query("SELECT c FROM Cover c JOIN c.song s JOIN s.artist a WHERE a.name LIKE %:keyword% ORDER BY c.createdAt DESC LIMIT 3")
    List<Cover> findTop3ByArtistNameContainingOrderByCreatedAtDesc(@Param("keyword") String keyword);

    //최신 컨텐츠를 위한 10개 가져옴
    List<Cover> findTop10ByOrderByCreatedAtDesc();

    @Modifying
    @Query("UPDATE Cover c SET c.weeklyHit = 0")
    void resetWeeklyHits();

}
