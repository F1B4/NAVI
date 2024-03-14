package ssafy.navi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Voice {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voice_pk")
    private Long id;

    @Column(name = "path")
    private String path;

    @OneToMany(mappedBy = "voice", cascade = CascadeType.ALL)
    private List<User> users;

    @OneToMany(mappedBy = "voice")
    private List<Song> songs;
}
