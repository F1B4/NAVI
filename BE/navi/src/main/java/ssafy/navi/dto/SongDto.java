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
    private ArtistDto artist;
    private List<NoraebangDto> noraebangDtos;
    private List<MatchingDto> matchingDtos;
    private List<LyricDto> lyricDtos;
    private List<PartDto> partDtos;

    // 엔티티 Dto로 변환
    public static SongDto convertToDto(Song song) {
        SongDto songDto = new SongDto();

        // set
        songDto.setId(song.getId());
        songDto.setTitle(song.getTitle());
        songDto.setMr(song.getMr());
        songDto.setImage(song.getImage());
        songDto.setArtist(ArtistDto.convertToDto(song.getArtist()));
        songDto.setNoraebangDtos(song.getNoraebangs()
                .stream().map(NoraebangDto::convertToDto)
                .collect(Collectors.toList()));
        songDto.setMatchingDtos(song.getMatchings()
                .stream().map(MatchingDto::convertToDto)
                .collect(Collectors.toList()));
        songDto.setLyricDtos(song.getLyrics()
                .stream().map(LyricDto::convertToDto)
                .collect(Collectors.toList()));
        songDto.setPartDtos(song.getParts()
                .stream().map(PartDto::convertToDto)
                .collect(Collectors.toList()));

        return songDto;
    }
}