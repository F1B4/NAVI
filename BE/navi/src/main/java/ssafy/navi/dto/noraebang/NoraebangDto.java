package ssafy.navi.dto.noraebang;

import lombok.*;
import ssafy.navi.dto.song.SongDto;
import ssafy.navi.dto.util.TimeDto;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.entity.noraebang.Noraebang;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NoraebangDto implements TimeDto {

    private Long id;
    private String content;
    private String record;
    private Integer hit;
    private Integer likeCount;
    private SongDto songDto;
    private UserDto userDto;
    private LocalDateTime createdAt;
    private List<NoraebangLikeDto> noraebangLikeDtos;
    private List<NoraebangReviewDto> noraebangReviewDtos;

    // 엔티티 Dto로 변환

    public static NoraebangDto convertToDtoDetail(Noraebang noraebang) {
        NoraebangDto noraebangDto = new NoraebangDto();

        // set
        noraebangDto.setId(noraebang.getId());
        noraebangDto.setContent(noraebang.getContent());
        noraebangDto.setRecord(noraebang.getRecord());
        noraebangDto.setHit(noraebang.getHit());
        noraebangDto.setLikeCount(noraebang.getLikeCount());
        noraebangDto.setUserDto(UserDto.convertToDto(noraebang.getUser()));
        noraebangDto.setSongDto(SongDto.convertToDto(noraebang.getSong()));
        noraebangDto.setNoraebangLikeDtos(noraebang.getNoraebangLikes()
                .stream().map(NoraebangLikeDto::convertToDto)
                .collect(Collectors.toList()));
        noraebangDto.setNoraebangReviewDtos(noraebang.getNoraebangReviews()
                .stream().map(NoraebangReviewDto::convertToDto)
                .collect(Collectors.toList()));

        return noraebangDto;
    }
    public static NoraebangDto convertToDtoNoraebangs(Noraebang noraebang) {
        NoraebangDto noraebangDto = new NoraebangDto();

        // set
        noraebangDto.setId(noraebang.getId());
        noraebangDto.setSongDto(SongDto.convertToDto(noraebang.getSong()));
        noraebangDto.setUserDto(UserDto.convertToDto(noraebang.getUser()));
        noraebangDto.setCreatedAt(noraebang.getCreatedAt());
        return noraebangDto;
    }
    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
