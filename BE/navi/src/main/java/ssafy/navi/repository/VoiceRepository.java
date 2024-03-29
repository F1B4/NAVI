package ssafy.navi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssafy.navi.entity.user.Voice;

public interface VoiceRepository extends JpaRepository<Voice, Long> {
}
