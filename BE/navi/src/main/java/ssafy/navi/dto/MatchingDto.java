package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.AlarmStatus;
import ssafy.navi.entity.Matching;
import ssafy.navi.entity.MatchingStatus;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MatchingDto {

    private Long id;
    private MatchingStatus status;
    private SongDto song;
    private PartDto part;
    private List<CoverDto> coverDtos;
    private List<MatchingUserDto> matchingUserDtos;

    // 엔티티 Dto로 변환
    public static MatchingDto convertToDto(Matching matching) {
        MatchingDto matchingDto = new MatchingDto();

        // set
        matchingDto.setId(matching.getId());
        matchingDto.setStatus(matching.getStatus());
        matchingDto.setSong(SongDto.convertToDto(matching.getSong()));
        matchingDto.setPart(PartDto.convertToDto(matching.getPart()));
        matchingDto.setCoverDtos(matching.getCovers()
                .stream().map(CoverDto::convertToDto)
                .collect(Collectors.toList()));
        matchingDto.setMatchingUserDtos(matching.getMatchingUsers()
                .stream().map(MatchingUserDto::convertToDto)
                .collect(Collectors.toList()));

        return matchingDto;
    }
}
