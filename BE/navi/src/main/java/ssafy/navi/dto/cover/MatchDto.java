package ssafy.navi.dto.cover;

import lombok.*;
import ssafy.navi.entity.cover.Matching;
import ssafy.navi.entity.song.Song;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MatchDto {
    private int partCount;
    private int artists;
    private String title;
    private String image;
    private String groupName;

    public static MatchDto convertToDto(Matching matching) {
        MatchDto matchDto = new MatchDto();
        Song song = matching.getSong();

        matchDto.setPartCount(matching.getPartCount());
        matchDto.setArtists(song.getArtist().getPartCount());
        matchDto.setTitle(song.getTitle());
        matchDto.setImage(song.getImage());
        matchDto.setGroupName(song.getArtist().getName());

        return matchDto;
    }
}
