package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.Matching;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MatchingDto {

    private Long id;
//    private SongDto songDto;
//    private List<MatchingUserDto> matchingUserDtos;

    // 엔티티 Dto로 변환
    public static MatchingDto convertToDto(Matching matching) {
        MatchingDto matchingDto = new MatchingDto();

        // set
        matchingDto.setId(matching.getId());
//        matchingDto.setSongDto(SongDto.convertToDto(matching.getSong()));
//        matchingDto.setMatchingUserDtos(matching.getMatchingUsers()
//                .stream().map(MatchingUserDto::convertToDto)
//                .collect(Collectors.toList()));

        return matchingDto;
    }
}
