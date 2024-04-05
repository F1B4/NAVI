package ssafy.navi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssafy.navi.dto.song.SongDto;
import ssafy.navi.entity.song.Artist;
import ssafy.navi.repository.ArtistRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SongService {

    private final ArtistRepository artistRepository;

    /*
    해당 원곡자의 모든 노래 조회
     */
    public List<SongDto> getSongByArtist(Long artistPk) {
        Artist artist = artistRepository.findById(artistPk)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + artistPk));

        return artist.getSongs().stream()
                .map(SongDto::convertToDto)
                .collect(Collectors.toList());
    }

}
