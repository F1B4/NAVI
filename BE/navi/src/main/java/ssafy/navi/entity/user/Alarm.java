package ssafy.navi.entity.user;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import ssafy.navi.entity.util.BaseTimeEntity;
import ssafy.navi.service.AlarmService;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE) //builder패턴을 사용하기 위해 추가
public class Alarm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="alarm_pk")
    private Long id;

    @Column(name="content")
    private String content;

    @Column(name = "alarm_status")
    private AlarmStatus alarmStatus;

    //==외래키==//

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User user;

    public void updateAlarmStatus(AlarmStatus alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

}
