package ssafy.navi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ssafy.navi.dto.user.AlarmDto;
import ssafy.navi.dto.util.Response;
import ssafy.navi.service.AlarmService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {

    private final AlarmService alarmService;


    @GetMapping("")
    public Response<List<AlarmDto>> getAlarms() throws Exception {
        return Response.of("OK", "모든 알람 가져오기.", alarmService.getAlarms());
    }


    @DeleteMapping("/{alarm_pk}")
    public Response<?> deleteAlarm(@PathVariable("alarm_pk") Long alarmPk) {
        alarmService.deleteAlarm(alarmPk);
        return Response.of("OK", "알람을 삭제 했습니다.", null);
    }

    @GetMapping("/{alarm_pk}")
    public Response<AlarmDto> getAlarmDetail(@PathVariable("alarm_pk") Long alarmPk) throws Exception {
        return Response.of("OK", "알람 디테일을 조회 했습니다.", alarmService.getAlarmDetail(alarmPk));
    }

}
