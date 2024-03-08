package ssafy.navi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Matching extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_pk")
    private Long id;

    @Column(name = "status")
    private MatchingStatus status;
    
    //==외래키==//
    
    // 노래
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_pk")
    private Song song;

    // 파트
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_pk")
    private Part part;
    
    // 커버 게시글
    @OneToMany(mappedBy = "matching")
    private List<Cover> covers;
    
    // 매칭 중계 테이블
    @OneToMany(mappedBy = "matching")
    private List<MatchingUser> matchingUsers;

}
