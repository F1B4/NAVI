package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.Alarm;
import ssafy.navi.entity.AlarmStatus;


@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AlarmDto {

    private Long id;
    private String content;
    private AlarmStatus alarmStatus;
//    private UserDto userDto;

    // 엔티티 Dto로 변환
    public static AlarmDto convertToDto(Alarm alarm) {
        AlarmDto alarmDto = new AlarmDto();

        // set
        alarmDto.setId(alarm.getId());
        alarmDto.setContent(alarm.getContent());
        alarmDto.setAlarmStatus(alarm.getAlarmStatus());
//        alarmDto.setUserDto(UserDto.convertToDto(alarm.getUser()));

        return alarmDto;
    }
}
