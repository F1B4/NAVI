package ssafy.navi.dto.cover;

import lombok.*;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.entity.cover.CoverReview;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CoverReviewDto {

    private Long id;
    private String content;
    private UserDto userDto;

    // 엔티티 Dto로 변환
    public static CoverReviewDto convertToDto(CoverReview coverReview) {
        CoverReviewDto coverReviewDto = new CoverReviewDto();

        // set
        coverReviewDto.setId(coverReview.getId());
        coverReviewDto.setContent(coverReview.getContent());
        coverReviewDto.setUserDto(UserDto.convertToDtoCoverDetail(coverReview.getUser()));

        return coverReviewDto;
    }
}
