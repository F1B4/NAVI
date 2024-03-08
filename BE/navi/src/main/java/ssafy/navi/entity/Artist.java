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
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_pk")
    private Long id;

    // 원곡자 이름
    @Column(name = "name")
    private String name;

    // 파트 수
    @Column(name = "part_count")
    private Integer partCount;

    // 가수가 부른 노래들
    @OneToMany(mappedBy = "artist")
    private List<Song> songs;

}
