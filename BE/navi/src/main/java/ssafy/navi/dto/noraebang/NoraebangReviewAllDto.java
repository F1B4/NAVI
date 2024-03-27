package ssafy.navi.dto.noraebang;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.entity.noraebang.NoraebangReview;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoraebangReviewAllDto {
    private Long id;
    private String content;
    private Long userId;
    private String nickname;
    private String Image;

    public static NoraebangReviewAllDto convertToDto(NoraebangReview noraebangReview) {
        NoraebangReviewAllDto noraebangReviewDto = new NoraebangReviewAllDto();

        // set
        noraebangReviewDto.setId(noraebangReview.getId());
        noraebangReviewDto.setContent(noraebangReview.getContent());
        noraebangReviewDto.setUserId(noraebangReview.getUser().getId());
        noraebangReviewDto.setNickname(noraebangReview.getUser().getNickname());
        noraebangReviewDto.setImage(noraebangReview.getUser().getImage());

        return noraebangReviewDto;
    }
}
