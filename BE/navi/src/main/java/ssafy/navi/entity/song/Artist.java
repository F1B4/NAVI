package ssafy.navi.entity.song;

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
    @Column(name="artist_pk")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="part_count")
    private Integer partCount;

    //==외래키==//

    @OneToMany(mappedBy = "artist")
    private List<Song> songs;
}
