package ssafy.navi.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssafy.navi.dto.noraebang.NoraebangDto;
import ssafy.navi.dto.noraebang.NoraebangReviewDto;
import ssafy.navi.entity.noraebang.Noraebang;
import ssafy.navi.entity.noraebang.NoraebangLike;
import ssafy.navi.entity.noraebang.NoraebangReview;
import ssafy.navi.entity.song.Artist;
import ssafy.navi.entity.song.Song;
import ssafy.navi.entity.user.User;
import ssafy.navi.repository.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoraebangService {

    private final NoraebangRepository noraebangRepository;
    private final SongRepository songRepository;

    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final NoraebangReviewRepository noraebangReviewRepository;
    private final NoraebangLikeRepository noraebangLikeRepository;

    /*
    모든 노래방 게시글 가져오기
     */
    public List<NoraebangDto> getNoraebang() {
        List<Noraebang> noraebangs = noraebangRepository.findAll();

        return noraebangs.stream()
                .map(NoraebangDto::convertToDtoNoraebangs)
                .toList();
    }

    /*
    노래방 게시글 디테일 정보 가져오기
     */
    public NoraebangDto getNoraebangDetail(Long pk) {
        Noraebang noraebang = noraebangRepository.findById(pk)
                .orElseThrow(() -> new EntityNotFoundException("Norabang not found with id: " + pk));

        return NoraebangDto.convertToDtoDetail(noraebang);
    }

    /*
     게시글 작성하기.
     formData 형식으로 file과 게시글 내용, songPk, userPk필요
     */
    public void createNoraebang(MultipartFile file, String content, Long songPk, Long userPk) throws IOException {
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

    /*
    노래방 게시글 내용 수정하기.
     */
    public void updateNoraebang(String content, Long NoraebangPk) {
        Optional<Noraebang> byId = noraebangRepository.findById(NoraebangPk);
        byId.ifPresent(norae -> {
            norae.updateContent(content);
        });
    }

    /*
    노래방 게시글 삭제하기
     */
    public void deleteNoraebang(Long noraebangPk) {
        noraebangRepository.deleteById(noraebangPk);
    }

    /*
    노래방 게시글 댓글달기,
    게시글 pk, 유저 pk, 댓글 내용 필요.
     */
    @Transactional
    public void createNoraebangReview(Long noraebangPk, NoraebangReviewDto noraebangReviewDto) {
        Long userPk = noraebangReviewDto.getUserDto().getId();
        String content = noraebangReviewDto.getContent();
        Optional<Noraebang> noraebangOptional = noraebangRepository.findById(noraebangPk);
        Optional<User> userOptional = userRepository.findById(userPk);

        if (noraebangOptional.isPresent() && userOptional.isPresent()) {
            Noraebang noraebang = noraebangOptional.get();
            User user = userOptional.get();

            NoraebangReview review = NoraebangReview.builder()
                    .user(user)
                    .content(content)
                    .noraebang(noraebang)
                    .build();

            noraebangReviewRepository.save(review);
        }
    }

    /*
    게시글 댓글 모두 조회
     */
    public List<NoraebangReviewDto> getNoraebangReviews(Long noraebangPk) {
        Noraebang noraebang = noraebangRepository.getById(noraebangPk);
        List<NoraebangReview> noraebangReviews = noraebang.getNoraebangReviews();
        return noraebangReviews.stream()
                .map(NoraebangReviewDto::convertToDto)
                .collect(Collectors.toList());
    }

    /*
    게시글 댓글 삭제.
    작성자만 삭제할 수 있음.
     */
    public String deleteNoraebangReview(Long reviewPk, Long userPk) {
        NoraebangReview review = noraebangReviewRepository.getById(reviewPk);

        if (!Objects.equals(review.getUser().getId(), userPk)) {
            return "댓글이 존재하지 않음";
        }
        noraebangReviewRepository.deleteById(reviewPk);

        return "댓글 삭제 완료";
    }

    /*
    게시글 좋아요 기능.
     */
    public void toggleNoraebangLike(Long noraebangPk, Long userPk) {
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
