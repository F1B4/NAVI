package ssafy.navi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssafy.navi.dto.user.AlarmDto;
import ssafy.navi.entity.user.Alarm;
import ssafy.navi.entity.user.AlarmStatus;
import ssafy.navi.entity.user.User;
import ssafy.navi.repository.AlarmRespository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmService {

    private final AlarmRespository alarmRespository;
    private final UserService userService;

    public List<AlarmDto> getAlarms() throws Exception {
        User user = userService.findById(Long.valueOf(3));
        List<Alarm> alarms = user.getAlarms();
        return alarms.stream()
                .map(AlarmDto::convertToDto)
                .toList();
    }


    public void deleteAlarm(Long alarmPk) {
        alarmRespository.deleteById(alarmPk);
    }


    public void createAlarm(Long userPk, String message) throws Exception {
        User user = userService.findById(userPk);
        Alarm alarm = Alarm.builder()
                .user(user)
                .content(message)
                .alarmStatus(AlarmStatus.UNREAD)
                .build();

        alarmRespository.save(alarm);
    }

    @Transactional
    public AlarmDto getAlarmDetail(Long alarmPk) throws Exception {
        Alarm alarm = alarmRespository.findById(alarmPk)
                .orElseThrow(() -> new Exception("알람이 없음"));
        alarm.updateAlarmStatus(AlarmStatus.READ);

        return AlarmDto.convertToDto(alarm);
    }

}
