package ssafy.navi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.navi.entity.NoraebangReview;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoraebangReviewDto {

    private Long id;
    private String content;
    private UserDto user;
    private NoraebangDto noraebang;

    // 엔티티 Dto로 변환
    public static NoraebangReviewDto convertToDto(NoraebangReview noraebangReview) {
        NoraebangReviewDto noraebangReviewDto = new NoraebangReviewDto();

        // set
        noraebangReviewDto.setId(noraebangReview.getId());
        noraebangReviewDto.setContent(noraebangReview.getContent());
        noraebangReviewDto.setUser(UserDto.convertToDto(noraebangReview.getUser()));
        noraebangReviewDto.setNoraebang(NoraebangDto.convertToDto(noraebangReview.getNoraebang()));

        return noraebangReviewDto;
    }
}
