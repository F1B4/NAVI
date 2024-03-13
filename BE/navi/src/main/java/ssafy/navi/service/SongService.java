package ssafy.navi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssafy.navi.dto.SongDto;
import ssafy.navi.entity.Song;
import ssafy.navi.repository.NoraebangRepository;
import ssafy.navi.repository.SongRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }


    public List<SongDto> getAllSongs() {
        List<Song> all = songRepository.findAll();

        return all.stream()
                .map(SongDto::convertToDtoArtist)
                .collect(Collectors.toList());
    }
}
