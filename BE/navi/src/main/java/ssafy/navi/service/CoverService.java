package ssafy.navi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ssafy.navi.dto.*;
import ssafy.navi.entity.*;
import ssafy.navi.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoverService {

    private final CoverRepository coverRepository;
    private final CoverReviewRepository coverReviewRepository;
    private final CoverUserRepository coverUserRepository;
    private final UserRepository userRepository;
    private final CoverLikeRepository coverLikeRepository;
    private final NoraebangRepository noraebangRepository;
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private final PartRepository partRepository;
    private final FollowRepository followRepository;
    private final MatchingRepository matchingRepository;
    private final MatchingUserRepository matchingUserRepository;

    /*
    커버 게시판 전체 게시글 조회
     */
    public List<CoverDto> getCover() {
        //커버의 모든 게시글을 조회하고 최신순으로 정렬함
        List<Cover> covers = coverRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return covers.stream()
                .map(CoverDto::convertToDtoList)
                .collect(Collectors.toList());
    }

    /*
    커버 게시판 전체 게시글 조회
    조회순
     */
    public List<CoverDto> getCoverByView() {
        //커버의 모든 게시글을 조회하는데 조회수 순으로 정렬함
        List<Cover> covers = coverRepository.findAll(Sort.by(Sort.Direction.DESC, "hit")); // 조회수 내림차순 정렬
        return covers.stream()
                .map(CoverDto::convertToDto)
                .collect(Collectors.toList());
    }

    /*
    커버 게시판 전체 게시글 조회
    좋아요 순
     */
    public List<CoverDto> getCoverByLike(){
        List<Cover> covers=coverRepository.findAll(Sort.by(Sort.Direction.DESC,"likeCount"));   //좋아요 내림차순 정렬
        return covers.stream()
                .map(CoverDto::convertToDto)
                .collect(Collectors.toList());
    }

    /*
    아티스트 전체 조회
     */
    public List<ArtistDto> getArtist(){
        List<Artist> artists=artistRepository.findAll(Sort.by(Sort.Direction.ASC,"name")); //이름을 내림차순 정렬
        return artists.stream()
                .map(ArtistDto::convertToDto)
                .collect(Collectors.toList());
    }

    /*
    아티스트의 전체 노래 조회
     */
    public List<SongDto> getSongs(Long artistPk) throws Exception{
        return songRepository.findById(artistPk)
                .stream()
                .map(SongDto::convertToDto)
                .collect(Collectors.toList());
    }


    /*
    파트 및 맞팔로우 목록 가져오기
     */
    public Map<String,Object> getPartAndMutualFollow(Long songPk) throws Exception{
        Map<String,Object> partAndMutualFollow=new HashMap<>();
        List<PartDto> parts=partRepository.findBySongId(songPk)
                .stream()
                .map(PartDto::convertToDto)
                .collect(Collectors.toList());

        List<UserDto> mutualFollow=followRepository.findMutualFollowers(1L)
                .stream()
                .map(UserDto::convertToDto)
                .collect(Collectors.toList());

        partAndMutualFollow.put("part",parts);
        partAndMutualFollow.put("mutualFollow",mutualFollow);

        return partAndMutualFollow;
    }

    /*
    커버 생성 로직
     */
    public String createCover(CoverRegistDto coverRegistDto){
        //현재 사용자가 요청한 파트 수
        int matchingCount= coverRegistDto.getPartData().size();
        Song song=songRepository.findById(coverRegistDto.getSongPk())
                .orElseThrow(()->new RuntimeException("해당 곡이 없습니다."));
        Artist artist=song.getArtist();
        int totalPartCount=artist.getPartCount();

        List<Matching> matchings=matchingRepository.findBySongId(coverRegistDto.getSongPk());
        for(Matching matching : matchings){
            int existingPartCount=matching.getMatchingUsers().size();
            if(existingPartCount+matchingCount<=totalPartCount){
                for(UserPartDto userPart : coverRegistDto.getPartData()){
                    
                }
            }
        }
        return "matching";
    }

    /*
    최신 컨텐츠 가져오기
     */
    public List<TimeDto> getNewContents(){
        List<TimeDto> covers=coverRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(CoverDto::convertToDtoList)
                .collect(Collectors.toList());
        List<TimeDto> noraebangs=noraebangRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(NoraebangDto::convertToDtoNoraebangs)
                .collect(Collectors.toList());
        List<TimeDto> newList = new ArrayList<>(covers);
        newList.addAll(noraebangs);
        // 리스트를 생성시간으로 내림차순 정렬
        newList.sort(Comparator.comparing(TimeDto::getCreatedAt).reversed());

        // 정렬된 리스트에서 최신 10개의 데이터만 반환
        return newList.stream().limit(10).collect(Collectors.toList());
    }



    /*
    Hot 게시글 조회하기
    오늘 날짜로 부터 1주일 이내의 게시글 중 조회수가 가장 높은 6개를 조회함
    LocalDate타입에서 LocalDateTime으로 변환하는 이유 : 1주일 전의 자정을 기준으로 조회하기 위해서 시간개념이 포함된 LocalDateTime으로 변환함
     */
    public List<CoverDto> getHotCover() {
        //1주일 전 날짜를 알아냄
        LocalDate oneWeek = LocalDate.now().minus(Period.ofWeeks(1));
        //1주일 전 날짜의 자정으로 값 지정
        LocalDateTime oneWeekAgo = oneWeek.atStartOfDay();
        List<Cover> covers = coverRepository.findTop6ByCreatedAtAfterOrderByWeeklyHitDesc(oneWeekAgo);
        return covers.stream()
                .map(CoverDto::convertToDtoList)
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0 0 * * SUN")
    @Transactional
    public void resetWeeklyHits() {
        coverRepository.resetWeeklyHits();
    }

    /*
    orElseThrow를 통해 값이 없을경우 예외처리를 함
    orElseThrow를 사용하는 이유 -> jpa는 optional타입으로 반환하기 때문.
    커버 정보, 커버 댓글, 원곡 정보, 각자 맡은 파트
    원곡 정보는 Cover에 포함되어 있기 때문에 따로 선언하지 않음.
    Map을 사용하는 이유, 여러 Dto를 한번에 보내주기 위해서
     */
    public Map<String, Object> getCoverDetail(Long coverPk) throws Exception {
        Map<String, Object> coverDetail = new HashMap<>();
        CoverDto coverDto = coverRepository.findById(coverPk).map(cover -> {
                    cover.setHit(cover.getHit() + 1); // 조회수 증가
                    cover.setWeeklyHit(cover.getWeeklyHit()+1); //주간 조회수 증가
                    return coverRepository.save(cover); // 변경된 엔티티 저장
                })
                .map(CoverDto::convertToDto).orElseThrow(() -> new Exception("커버가 없어요"));

        List<CoverUserDto> coverUserDtos = coverUserRepository.findByCover_Id(coverPk)
                .stream()
                .map(CoverUserDto::convertToDto)
                .collect(Collectors.toList());

        coverDetail.put("cover", coverDto);
        coverDetail.put("CoverUser", coverUserDtos);
        return coverDetail;
    }

    /*
    유저와 게시글이 있는지 확인하고 있다면 해당 값을 댓글에 값을 세팅하고 db에 추가함
    게시글이 존재하는지 체크 -> 유저가 존재하는지 체크 -> 둘다 충족한다면 받아온 내용을 저장하고 저장되는 내용을 반환함
     */
    public CoverReviewDto createCoverReview(Long coverPk, CoverReviewDto coverReviewDto) throws Exception {
        Cover cover = coverRepository.findById(coverPk)
                .orElseThrow(() -> new Exception("커버 게시글이 존재하지 않아요"));
        User user = userRepository.findById(Long.valueOf(1))
                .orElseThrow(() -> new Exception("유저가 없어요"));
        CoverReview coverReview = new CoverReview(coverReviewDto.getContent(), cover, user);
        coverReview = coverReviewRepository.save(coverReview);
        return CoverReviewDto.convertToDto(coverReview);
    }

    /*
    게시글 체크, 유저 체크, 댓글 유무 체크
    댓글 삭제
     */
    public String deleteCoverReview(Long coverPk, Long coverReviewPk) throws Exception {
        Cover cover = coverRepository.findById(coverPk)
                .orElseThrow(() -> new Exception("커버 게시글이 존재하지 않아요"));
        User user = userRepository.findById(Long.valueOf(1))
                .orElseThrow(() -> new Exception("유저가 없어요"));
        CoverReview coverReview = coverReviewRepository.findById(coverReviewPk)
                .orElseThrow(() -> new Exception("댓글이 없어요"));
        coverReviewRepository.delete(coverReview);
        String msg = "댓글 삭제가 잘 되었네요~";
        return msg;
    }

    /*
    커버 좋아요
    커버 체크, 유저 체크, 이미 좋아요를 눌렀다면 좋아요 취소
    좋아요를 누르지 않았다면 좋아요 생성
     */
    public CoverLikeDto coverLike(Long coverPk) throws Exception {
        Cover cover = coverRepository.findById(coverPk)
                .orElseThrow(() -> new Exception("커버 게시글이 존재하지 않아요"));
        User user = userRepository.findById(Long.valueOf(1))
                .orElseThrow(() -> new Exception("유저가 없어요"));
        Optional<CoverLike> exists = coverLikeRepository.findByCoverAndUser(cover, user);

        if (exists.isPresent()) {
            // 이미 좋아요를 눌렀다면, 좋아요 삭제
            coverLikeRepository.delete(exists.get());
            int likeCount = cover.getLikeCount() == null ? 0 : cover.getLikeCount() - 1;
            cover.setLikeCount(likeCount);
            coverRepository.save(cover);
            return null;
        } else {
            // 좋아요가 없다면, 새로운 좋아요 생성
            CoverLike like = new CoverLike(cover, user);
            coverLikeRepository.save(like);
            int likeCount = cover.getLikeCount() == null ? 1 : cover.getLikeCount() + 1;
            cover.setLikeCount(likeCount);
            coverRepository.save(cover);
            return CoverLikeDto.convertToDto(like);
        }
    }
}
