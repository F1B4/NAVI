package ssafy.navi.entity.cover;

import jakarta.persistence.*;
import lombok.*;
import ssafy.navi.entity.song.Song;
import ssafy.navi.entity.util.BaseTimeEntity;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Matching extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_pk")
    private Long id;

    @Column(name="part_count")
    private int partCount;

    //==외래키==//
    
    // 노래
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_pk")
    private Song song;

    // 매칭 중계 테이블
    @OneToMany(mappedBy = "matching",cascade = CascadeType.ALL)
    private List<MatchingUser> matchingUsers;

    @Builder
    public Matching(int partCount, Song song){
        this.partCount=partCount;
        this.song=song;
    }
}
