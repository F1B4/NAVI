package ssafy.navi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ssafy.navi.entity.Song;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SongDto {

    private Long id;
    private String title;
    private String mr;
    private String image;
    private String artist;
    private Integer partCount;

    // 엔티티 Dto로 변환
    public static SongDto convertToDto(Song song) {
        SongDto songDto = new SongDto();

        // set
        songDto.setId(song.getId());
        songDto.setTitle(song.getTitle());
        songDto.setMr(song.getMr());
        songDto.setImage(song.getImage());
        songDto.setArtist(song.getArtist());
        songDto.setPartCount(song.getPartCount());

        return songDto;
    }
}