package ssafy.navi.dto.cover;

import lombok.*;
import ssafy.navi.entity.cover.Matching;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MatchingDto {

    private Long id;

    // 엔티티 Dto로 변환
    public static MatchingDto convertToDto(Matching matching) {
        MatchingDto matchingDto = new MatchingDto();

        // set
        matchingDto.setId(matching.getId());

        return matchingDto;
    }
}
