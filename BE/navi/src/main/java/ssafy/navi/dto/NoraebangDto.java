package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.Noraebang;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NoraebangDto {

    private Long id;
    private String title;
    private String content;
    private String record;
    private Integer hit;
    private Integer likeCount;
//    private UserDto userDto;
//    private SongDto songDto;
//    private List<NoraebangLikeDto> noraebangLikeDtos;
//    private List<NoraebangReviewDto> noraebangReviewDtos;

    // 엔티티 Dto로 변환
    public static NoraebangDto convertToDto(Noraebang noraebang) {
        NoraebangDto noraebangDto = new NoraebangDto();

        // set
        noraebangDto.setId(noraebang.getId());
        noraebangDto.setTitle(noraebang.getTitle());
        noraebangDto.setContent(noraebang.getContent());
        noraebangDto.setRecord(noraebang.getRecord());
        noraebangDto.setHit(noraebang.getHit());
        noraebangDto.setLikeCount(noraebang.getLikeCount());
//        noraebangDto.setUserDto(UserDto.convertToDto(noraebang.getUser()));
//        noraebangDto.setSongDto(SongDto.convertToDto(noraebang.getSong()));
//        noraebangDto.setNoraebangLikeDtos(noraebang.getNoraebangLikes()
//                .stream().map(NoraebangLikeDto::convertToDto)
//                .collect(Collectors.toList()));
//        noraebangDto.setNoraebangReviewDtos(noraebang.getNoraebangReviews()
//                .stream().map(NoraebangReviewDto::convertToDto)
//                .collect(Collectors.toList()));

        return noraebangDto;
    }
}
