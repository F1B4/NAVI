package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.CoverReview;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CoverReviewDto {

    private Long id;
    private String content;
//    private CoverDto cover;
//    private UserDto user;

    // 엔티티 Dto로 변환
    public static CoverReviewDto convertToDto(CoverReview coverReview) {
        CoverReviewDto coverReviewDto = new CoverReviewDto();

        // set
        coverReviewDto.setId(coverReview.getId());
        coverReviewDto.setContent(coverReview.getContent());
//        coverReviewDto.setCover(CoverDto.convertToDto(coverReview.getCover()));
//        coverReviewDto.setUser(UserDto.convertToDto(coverReview.getUser()));

        return coverReviewDto;
    }
}
