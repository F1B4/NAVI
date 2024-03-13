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
//    private UserDto userDto;
//    private NoraebangDto noraebangDto;

    // 엔티티 Dto로 변환
    public static NoraebangReviewDto convertToDto(NoraebangReview noraebangReview) {
        NoraebangReviewDto noraebangReviewDto = new NoraebangReviewDto();

        // set
        noraebangReviewDto.setId(noraebangReview.getId());
        noraebangReviewDto.setContent(noraebangReview.getContent());
//        noraebangReviewDto.setUserDto(UserDto.convertToDto(noraebangReview.getUser()));
//        noraebangReviewDto.setNoraebangDto(NoraebangDto.convertToDto(noraebangReview.getNoraebang()));

        return noraebangReviewDto;
    }
}
