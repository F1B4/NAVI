package ssafy.navi.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssafy.navi.dto.noraebang.*;
import ssafy.navi.dto.user.CustomOAuth2User;
import ssafy.navi.entity.cover.CoverLike;
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
    private final NotificationService notificationService;

    /*
    모든 노래방 게시글 가져오기
     */
    public List<NoraebangAllDto> getNoraebang() {
        List<Noraebang> noraebangs = noraebangRepository.findAll();

        return noraebangs.stream()
                .map(NoraebangAllDto::convertToDto)
                .toList();
    }

    /*
    노래방 게시글 디테일 정보 가져오기
     */
    public NoraebangDetailDto getNoraebangDetail(Long pk) {
        Noraebang noraebang = noraebangRepository.findById(pk)
                .orElseThrow(() -> new EntityNotFoundException("Norabang not found with id: " + pk));

        // 현재 인가에서 유저 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        User user = userRepository.findByUsername(customOAuth2User.getUsername());

        // 내가 이 게시물을 좋아요 했는지 안했는지 체크하는 부분
        Optional<NoraebangLike> exists = noraebangLikeRepository.findByNoraebangIdAndUserId(pk, user.getId());

        NoraebangDetailDto noraebangDetailDto = NoraebangDetailDto.convertToDto(noraebang);
        noraebangDetailDto.updateExists(exists.isPresent());

        return noraebangDetailDto;
    }


    /*
     게시글 작성하기.
     formData 형식으로 file과 게시글 내용, songPk, userPk필요
     */
    public void createNoraebang(MultipartFile file, String content, Long songPk) throws IOException {
        String fileName = s3Service.saveFile(file);
        Optional<Song> songbyId = songRepository.findById(songPk);
        if (songbyId.isPresent()) {
            Long artistId = songbyId.get().getArtist().getId();
            Optional<Artist> artistById = artistRepository.findById(artistId);
            // 현재 인가에서 유저 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
            User user = userRepository.findByUsername(customOAuth2User.getUsername());
            if (artistById.isPresent() && user!=null) {
                Noraebang noraebang = Noraebang.builder()
                        .content(content)
                        .record(fileName)
                        .user(user)
                        .song(songbyId.get())
                        .build();
                noraebangRepository.save(noraebang);
            }
        }
    }

    /*
    노래방 게시글 내용 수정하기.
     */
    @Transactional
    public void updateNoraebang(String content, Long NoraebangPk) {
        Optional<Noraebang> byId = noraebangRepository.findById(NoraebangPk);
        byId.ifPresent(norae -> {
            norae.updateContent(content);
        });
    }

    /*
    노래방 게시글 삭제하기
     */
    public void deleteNoraebang(Long noraebangPk) throws Exception {
        Noraebang noraebang = noraebangRepository.findById(noraebangPk)
                .orElseThrow(() -> new Exception("노래방 게시글을 찾을 수 없습니다."));
        s3Service.deleteImage(noraebang.getRecord());
        noraebangRepository.deleteById(noraebangPk);
    }

    /*
    노래방 게시글 댓글달기,
    게시글 pk, 댓글 내용 필요.
     */
    @Transactional
    public void createNoraebangReview(Long noraebangPk, NoraebangReviewDto noraebangReviewDto) throws Exception {
        String content = noraebangReviewDto.getContent();
        Optional<Noraebang> noraebangOptional = noraebangRepository.findById(noraebangPk);

        // 현재 인가에서 유저 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        User user = userRepository.findByUsername(customOAuth2User.getUsername());

        if (noraebangOptional.isPresent() && user!=null) {
            Noraebang noraebang = noraebangOptional.get();

            NoraebangReview review = NoraebangReview.builder()
                    .user(user)
                    .content(content)
                    .noraebang(noraebang)
                    .build();

            noraebangReviewRepository.save(review);
            notificationService.sendNotificationToUser(user.getId(), "노래방 게시글에 댓글이 작성 되었습니다.", "noraebangReview");
        }
    }

    /*
    게시글 댓글 모두 조회
     */
    public List<NoraebangReviewAllDto> getNoraebangReviews(Long noraebangPk) {
        Noraebang noraebang = noraebangRepository.getById(noraebangPk);
        List<NoraebangReview> noraebangReviews = noraebang.getNoraebangReviews();
        return noraebangReviews.stream()
                .map(NoraebangReviewAllDto::convertToDto)
                .collect(Collectors.toList());
    }

    /*
    게시글 댓글 삭제.
    작성자만 삭제할 수 있음.
     */
    public String deleteNoraebangReview(Long reviewPk) {
        NoraebangReview review = noraebangReviewRepository.getById(reviewPk);

        // 현재 인가에서 유저 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        User user = userRepository.findByUsername(customOAuth2User.getUsername());

        if (!Objects.equals(review.getUser().getId(), user.getId())) {
            return "댓글이 존재하지 않음";
        }
        noraebangReviewRepository.deleteById(reviewPk);

        return "댓글 삭제 완료";
    }

    /*
    게시글 좋아요 기능.
     */
    @Transactional
    public void toggleNoraebangLike(Long noraebangPk) {
        Noraebang noraebang = noraebangRepository.getById(noraebangPk);
        // 현재 인가에서 유저 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        User user = userRepository.findByUsername(customOAuth2User.getUsername());

        Optional<NoraebangLike> byNoraebangIdAndUserId = noraebangLikeRepository.findByNoraebangIdAndUserId(noraebangPk, user.getId());
        if (byNoraebangIdAndUserId.isPresent()) {
            noraebangLikeRepository.delete(byNoraebangIdAndUserId.get());
            Integer likeCount = noraebang.getLikeCount();
            noraebang.setLikeCount(likeCount-1);
        } else {
            NoraebangLike like = NoraebangLike.builder()
                    .noraebang(noraebang)
                    .user(user)
                    .build();
            Integer likeCount = noraebang.getLikeCount();
            noraebang.setLikeCount(likeCount+1);
            noraebangLikeRepository.save(like);
        }
    }

}
