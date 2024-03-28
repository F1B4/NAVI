package ssafy.navi.dto.noraebang;

import lombok.*;
import ssafy.navi.dto.song.LyricDto;
import ssafy.navi.dto.song.SongDto;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.entity.noraebang.Noraebang;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NoraebangDetailDto {
    private Long id;
    private String content;
    private String record;
    private Integer hit;
    private Integer likeCount;
    private SongDto songDto;
    private UserDto userDto;
    private LocalDateTime createdAt;
    private Boolean likeExsits;
    private List<NoraebangReviewAllDto> noraebangReviewDtos;
    private List<LyricDto> lyricDtos;

    public static NoraebangDetailDto convertToDto(Noraebang noraebang) {
        NoraebangDetailDto noraebangDto = new NoraebangDetailDto();

        // set
        noraebangDto.setId(noraebang.getId());
        noraebangDto.setContent(noraebang.getContent());
        noraebangDto.setRecord(noraebang.getRecord());
        noraebangDto.setHit(noraebang.getHit());
        noraebangDto.setCreatedAt(noraebang.getCreatedAt());
        noraebangDto.setLikeCount(noraebang.getLikeCount());
        noraebangDto.setUserDto(UserDto.convertToDto(noraebang.getUser()));
        noraebangDto.setSongDto(SongDto.convertToDto(noraebang.getSong()));
        noraebangDto.setNoraebangReviewDtos(noraebang.getNoraebangReviews()
                .stream().map(NoraebangReviewAllDto::convertToDto)
                .collect(Collectors.toList()));
        noraebangDto.setLyricDtos(noraebang.getSong().getLyrics()
                .stream().map(LyricDto::convertToDto)
                .collect(Collectors.toList()));
        return noraebangDto;
    }

    public void updateExists(Boolean likeExsits){
        this.likeExsits=likeExsits;
    }
}
