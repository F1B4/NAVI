package ssafy.navi.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssafy.navi.dto.NoraebangDto;
import ssafy.navi.dto.NoraebangReviewDto;
import ssafy.navi.dto.SongDto;
import ssafy.navi.entity.*;
import ssafy.navi.repository.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NoraebangService {

    private final NoraebangRepository noraebangRepository;
    private final SongRepository songRepository;

    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final NoraebangReviewRepository noraebangReviewRepository;
    private final NoraebangLikeRepository noraebangLikeRepository;

    @Autowired
    public NoraebangService(NoraebangRepository noraebangRepository,
                            SongRepository songRepository,
                            ArtistRepository artistRepository,
                            UserRepository userRepository,
                            S3Service s3Service,
                            NoraebangReviewRepository noraebangReviewRepository,
                            NoraebangLikeRepository noraebangLikeRepository) {
        this.noraebangRepository = noraebangRepository;
        this.songRepository = songRepository;
        this.artistRepository = artistRepository;
        this.userRepository = userRepository;
        this.s3Service = s3Service;
        this.noraebangReviewRepository = noraebangReviewRepository;
        this.noraebangLikeRepository = noraebangLikeRepository;
    }

    public List<NoraebangDto> getAllNoraebang() {
        List<Noraebang> all = noraebangRepository.findAll();

        return all.stream().map(NoraebangDto::convertToDtoNoraebangs).toList();
    }

    public NoraebangDto getNoraebang(Long pk) {
        Noraebang noraebang = noraebangRepository.findById(pk)
                .orElseThrow(() -> new EntityNotFoundException("Norabang not found with id: " + pk));

        return NoraebangDto.convertToDtoDetail(noraebang);
    }

    public void createNoraebang(MultipartFile file,
                                String content,
                                Long songPk,
                                Long userPk) throws IOException {
        String fileName = s3Service.saveFile(file);
        Optional<Song> songbyId = songRepository.findById(songPk);
        if (songbyId.isPresent()) {
            Long artistId = songbyId.get().getArtist().getId();
            Optional<Artist> artistById = artistRepository.findById(artistId);
            Optional<User> userById = userRepository.findById(userPk);
            if (artistById.isPresent() && userById.isPresent()) {
                Noraebang noraebang = Noraebang.builder()
                        .content(content)
                        .record(fileName)
                        .user(userById.get())
                        .song(songbyId.get())
                        .build();
                noraebangRepository.save(noraebang);
            }
        }
    }

    public void updateNoraebang(String content, Long NoraebangPk) {
        Optional<Noraebang> byId = noraebangRepository.findById(NoraebangPk);
        byId.ifPresent(norae -> {
            norae.setContent(content);
        });
    }

    public void deleteNoraebang(Long NoraebangPk) {
        noraebangRepository.deleteById(NoraebangPk);
    }

    @Transactional
    public void createReview(Long NoraebangPk,
                             Long userPk,
                             String content) {
        Optional<Noraebang> noraebangOptional = noraebangRepository.findById(NoraebangPk);
        Optional<User> userOptional = userRepository.findById(userPk);

        if (noraebangOptional.isPresent() && userOptional.isPresent()) {
            Noraebang noraebang = noraebangOptional.get();
            User user = userOptional.get();

            NoraebangReview review = NoraebangReview.builder()
                    .user(user)
                    .content(content)
                    .noraebang(noraebang)
                    .build();

            List<NoraebangReview> noraebangReviewsUser = user.getNoraebangReviews();
            List<NoraebangReview> noraebangReviewsNorabang = noraebang.getNoraebangReviews();

            noraebangReviewsUser.add(review);
            noraebangReviewsNorabang.add(review);

            user.setNoraebangReviews(noraebangReviewsUser);
            noraebang.setNoraebangReviews(noraebangReviewsNorabang);

            noraebangReviewRepository.save(review);
        }
    }

    public List<NoraebangReviewDto> getNoraebangReviews(Long NoraebangPk) {
        Noraebang noraebang = noraebangRepository.getById(NoraebangPk);
        List<NoraebangReview> noraebangReviews = noraebang.getNoraebangReviews();
        return noraebangReviews.stream().map(NoraebangReviewDto::convertToDto).collect(Collectors.toList());
    }

    public String deleteReview(Long reviewPk,
                               Long userPk) {
        NoraebangReview review = noraebangReviewRepository.getById(reviewPk);

        if (!Objects.equals(review.getUser().getId(), userPk)) {
            return "Fail";
        }
        noraebangReviewRepository.deleteById(reviewPk);

        return "Ok";
    }


    public void toggleNoraebangLike(Long noraebangPk,
                                    Long userPk) {
        Noraebang noraebang = noraebangRepository.getById(noraebangPk);
        Optional<User> userOptional = userRepository.findById(userPk);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Optional<NoraebangLike> byNoraebangIdAndUserId = noraebangLikeRepository.findByNoraebangIdAndUserId(noraebangPk, userPk);
            if (byNoraebangIdAndUserId.isPresent()) {
                noraebangLikeRepository.delete(byNoraebangIdAndUserId.get());
            } else {
                NoraebangLike like = NoraebangLike.builder()
                        .noraebang(noraebang)
                        .user(user)
                        .build();
                noraebangLikeRepository.save(like);
            }
        }
    }
}
