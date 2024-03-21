package ssafy.navi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssafy.navi.repository.SongRepository;

@Service
@Slf4j
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }


//    public List<SongDto> getAllSongs() {
//        List<Song> all = songRepository.findAll();
//
//        return all.stream()
//                .map(SongDto::convertToDtoArtist)
//                .collect(Collectors.toList());
//    }
}
