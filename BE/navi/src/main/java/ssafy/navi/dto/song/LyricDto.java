package ssafy.navi.dto.song;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.navi.entity.song.Lyric;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LyricDto {

    private Long id;
    private String startTime;
    private String endTime;
    private String content;
    private Integer sequence;
    private PartDto partDto;

    // 엔티티 Dto로 변환
    public static LyricDto convertToDto(Lyric lyric) {
        LyricDto lyricDto = new LyricDto();

        // set
        lyricDto.setId(lyric.getId());
        lyricDto.setStartTime(lyric.getStartTime());
        lyricDto.setEndTime(lyric.getEndTime());
        lyricDto.setContent(lyric.getContent());
        lyricDto.setSequence(lyric.getSequence());
        lyricDto.setPartDto(PartDto.convertToDto(lyric.getPart()));

        return lyricDto;
    }
}
