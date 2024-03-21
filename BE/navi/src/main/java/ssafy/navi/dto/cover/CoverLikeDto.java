package ssafy.navi.dto.cover;

import lombok.*;
import ssafy.navi.entity.cover.CoverLike;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CoverLikeDto {

    private Long id;

    // 엔티티 Dto로 변환
    public static CoverLikeDto convertToDto(CoverLike coverLike) {
        CoverLikeDto coverLikeDto = new CoverLikeDto();

        // set
        coverLikeDto.setId(coverLike.getId());

        return coverLikeDto;
    }
}
