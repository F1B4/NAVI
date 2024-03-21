package ssafy.navi.dto.noraebang;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.navi.entity.noraebang.NoraebangReview;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoraebangReviewDto {

    private Long id;
    private String content;

    // 엔티티 Dto로 변환
    public static NoraebangReviewDto convertToDto(NoraebangReview noraebangReview) {
        NoraebangReviewDto noraebangReviewDto = new NoraebangReviewDto();

        // set
        noraebangReviewDto.setId(noraebangReview.getId());
        noraebangReviewDto.setContent(noraebangReview.getContent());

        return noraebangReviewDto;
    }
}
