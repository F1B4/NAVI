package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.CoverLike;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CoverLikeDto {

    private Long id;
//    private CoverDto coverDto;
//    private UserDto userDto;

    // 엔티티 Dto로 변환
    public static CoverLikeDto convertToDto(CoverLike coverLike) {
        CoverLikeDto coverLikeDto = new CoverLikeDto();

        // set
        coverLikeDto.setId(coverLike.getId());
//        coverLikeDto.setCoverDto(CoverDto.convertToDto(coverLike.getCover()));
//        coverLikeDto.setUserDto(UserDto.convertToDto(coverLike.getUser()));

        return coverLikeDto;
    }
}
