package ssafy.navi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Entity
@Getter @Setter
@Validated
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoverUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cover_user_pk")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cover_pk")
    private Cover cover;
}
