package ssafy.navi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ssafy.navi.dto.user.AlarmDto;
import ssafy.navi.dto.user.CustomOAuth2User;
import ssafy.navi.entity.user.Alarm;
import ssafy.navi.entity.user.AlarmStatus;
import ssafy.navi.entity.user.User;
import ssafy.navi.repository.AlarmRespository;
import ssafy.navi.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmService {

    private final AlarmRespository alarmRespository;
    private final UserRepository userRepository;

    public List<AlarmDto> getAlarms() throws Exception {
        // 현재 인가에서 유저 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        User user = userRepository.findByUsername(customOAuth2User.getUsername());

        //알람 잘 오는지 확인용
//        User user = userRepository.findById(Long.valueOf(2)).get();

        List<Alarm> alarms = user.getAlarms();
        return alarms.stream()
                .map(AlarmDto::convertToDto)
                .toList();
    }


    public void deleteAlarm(Long alarmPk) {
        alarmRespository.deleteById(alarmPk);
    }


    public void createAlarm(Long userPk, String message) throws Exception {
        if (userRepository.findById(userPk).isPresent()) {
            User user = userRepository.findById(userPk).get();

            Alarm alarm = Alarm.builder()
                    .user(user)
                    .content(message)
                    .alarmStatus(AlarmStatus.UNREAD)
                    .build();
            alarmRespository.save(alarm);
        }
    }

    @Transactional
    public AlarmDto getAlarmDetail(Long alarmPk) throws Exception {
        Alarm alarm = alarmRespository.findById(alarmPk)
                .orElseThrow(() -> new Exception("알람이 없음"));
        alarm.updateAlarmStatus(AlarmStatus.READ);

        return AlarmDto.convertToDto(alarm);
    }

}
