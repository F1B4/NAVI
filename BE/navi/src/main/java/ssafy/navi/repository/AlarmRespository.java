package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.navi.entity.user.Alarm;

public interface AlarmRespository extends JpaRepository<Alarm, Long> {
}
