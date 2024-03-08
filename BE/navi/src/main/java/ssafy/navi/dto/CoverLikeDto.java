package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.CoverLike;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CoverLikeDto {

    private Long id;
    private CoverDto cover;
    private UserDto user;

    // 엔티티 Dto로 변환
    public static CoverLikeDto convertToDto(CoverLike coverLike) {
        CoverLikeDto coverLikeDto = new CoverLikeDto();

        // set
        coverLikeDto.setId(coverLike.getId());
        coverLikeDto.setCover(CoverDto.convertToDto(coverLike.getCover()));
        coverLikeDto.setUser(UserDto.convertToDto(coverLike.getUser()));

        return coverLikeDto;
    }
}
