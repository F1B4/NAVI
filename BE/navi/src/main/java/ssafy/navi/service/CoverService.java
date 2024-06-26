package ssafy.navi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ssafy.navi.dto.cover.*;
import ssafy.navi.dto.song.PartDto;
import ssafy.navi.dto.user.CustomOAuth2User;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.dto.util.Response;
import ssafy.navi.entity.cover.*;
import ssafy.navi.entity.song.Artist;
import ssafy.navi.entity.song.Part;
import ssafy.navi.entity.song.Song;
import ssafy.navi.entity.user.Role;
import ssafy.navi.entity.user.User;
import ssafy.navi.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoverService {

    private final CoverRepository coverRepository;
    private final CoverReviewRepository coverReviewRepository;
    private final UserRepository userRepository;
    private final CoverLikeRepository coverLikeRepository;
    private final SongRepository songRepository;
    private final PartRepository partRepository;
    private final FollowRepository followRepository;
    private final MatchingRepository matchingRepository;
    private final MatchingUserRepository matchingUserRepository;
    private final CoverUserRepository coverUserRepository;
    private final NotificationService notificationService;
    private final FastApiService fastApiService;
    private final UserService userService;

    /*
    커버 게시판 전체 게시글 조회
     */
    public List<CoverDto> getCover() {
        //커버의 모든 게시글을 조회하고 최신순으로 정렬함
        List<Cover> covers = coverRepository.findAllByVideoIsNotNull(Sort.by(Sort.Direction.DESC, "id"));
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
        List<Cover> covers = coverRepository.findAllByVideoIsNotNull(Sort.by(Sort.Direction.DESC, "hit")); // 조회수 내림차순 정렬
        return covers.stream()
                .map(CoverDto::convertToDto)
                .collect(Collectors.toList());
    }

    /*
    커버 게시판 전체 게시글 조회
    좋아요 순
     */
    public List<CoverDto> getCoverByLike(){
        List<Cover> covers=coverRepository.findAllByVideoIsNotNull(Sort.by(Sort.Direction.DESC,"likeCount"));   //좋아요 내림차순 정렬
        return covers.stream()
                .map(CoverDto::convertToDto)
                .collect(Collectors.toList());
    }

    /*
    파트 및 맞팔로우 목록 가져오기
     */
    public Map<String,Object> getPartAndMutualFollow(Long songPk) throws Exception{
        Map<String,Object> partAndMutualFollow=new HashMap<>();
        List<PartDto> parts=partRepository.findBySongId(songPk).stream()
                .map(PartDto::convertToDto)
                .collect(Collectors.toList());

        // 현재 인가에서 유저 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        User user = userRepository.findByUsername(customOAuth2User.getUsername());

        List<UserDto> mutualFollow=followRepository.findMutualFollowers(user.getId()).stream()
                .map(UserDto::convertToDto)
                .collect(Collectors.toList());

        partAndMutualFollow.put("part",parts);
        partAndMutualFollow.put("mutualFollow",mutualFollow);
        return partAndMutualFollow;
    }



    /*
    파트 가져오기
     */
    public List<PartDto> getPart(Long songPk) throws Exception{
        return partRepository.findBySongId(songPk).stream()
                .map(PartDto::convertToDto)
                .collect(Collectors.toList());
    }



    /*
    커버 생성 로직
     */
    @Transactional
    public Response<Long> createCover(CoverRegistDto coverRegistDto) throws Exception {

        //현재 사용자가 요청한 파트 수
        int matchingCount= coverRegistDto.getUserPartDtos().size();
        //전체 파트 수를 가져오기 위해서는 artist테이블까지 접근해야 하기 때문에 연관관계가 있는 song을 먼저 찾음
        Song song=songRepository.findById(coverRegistDto.getSongPk())
                .orElseThrow(()->new RuntimeException("해당 곡이 존재하지 않음"));
        //Artist에 파트 수가 저장되어 있음
        Artist artist=song.getArtist();
        //전체 파트 수
        int totalPartCount=artist.getPartCount();

        // 처음부터 모든 파트가 다 채워졌을 경우 바로 커버 생성
        if (matchingCount == totalPartCount) {
            Cover cover = Cover.builder()
                    .title(song.getTitle())
                    .song(song)
                    .build();
            coverRepository.save(cover);

            Set<Long> users= new HashSet<>();

            for (UserPartDto userPartDto : coverRegistDto.getUserPartDtos()) {
                User user = userRepository.findById(userPartDto.getUserPk())
                        .orElseThrow(() -> new RuntimeException("유저가 존재하지 않음"));
                Part part = partRepository.findById(userPartDto.getPartPk())
                        .orElseThrow(() -> new RuntimeException("파트가 존재하지 않음"));
                CoverUser newCoverUser = CoverUser.builder()
                        .user(user)
                        .cover(cover)
                        .part(part)
                        .build();
                coverUserRepository.save(newCoverUser);
                users.add(user.getId());
            }
            for (Long pk : users) {
                notificationService.sendNotificationToUser(pk, "커버 생성을 시작합니다.");
            }
//            fastApiService.fetchDataFromFastAPI("/ai/cover", cover.getId());
            return Response.of("OK", "1", cover.getId());
        }

        //해당 노래를 매칭중인 매칭테이블들 리스트
        List<Matching> matchings=matchingRepository.findBySongId(coverRegistDto.getSongPk());

        for(Matching matching : matchings){
            //해당 매칭에 현재 매칭된 사람의 수
            int existingPartCount=matching.getMatchingUsers().size();

            //해당 매칭의 사람 수 + 내가 매칭할 사람 수 <= 전체 파트 수라면 해당 매칭에 참여할 수 있는 1차 조건이 만족됨
            if(existingPartCount+matchingCount<=totalPartCount){
                List<Long> existsPart=matching.getMatchingUsers()
                        .stream()
                        .map(part->part.getPart().getId())
                        .collect(Collectors.toList());
                //내가 해당 매칭에 참여할 수 있는지 상태를 저장하는 boolean 변수
                boolean partCheck=true;

                for(UserPartDto userPart : coverRegistDto.getUserPartDtos()){
                    //내가 맡은 파트가 해당 매칭에 이미 존재하는 파트라면 해당 매칭에 참가할 수 없음
                    if(existsPart.contains(userPart.getPartPk())){
                        partCheck=false;
                        break;
                    }
                }

                //내가 이 매칭에 들어갈 수 있음
                if(partCheck){
                    for(UserPartDto userPartDto : coverRegistDto.getUserPartDtos()){
                        User user=userRepository.findById(userPartDto.getUserPk())
                                .orElseThrow(()->new RuntimeException("유저가 존재하지 않음"));
                        Part part=partRepository.findById(userPartDto.getPartPk())
                                .orElseThrow(()->new RuntimeException("파트가 존재하지 않음"));
                        //매칭 유저에 데이터 추가하기
                        MatchingUser newMatchingUser=MatchingUser.builder()
                                .user(user)
                                .matching(matching)
                                .part(part)
                                .build();
                        matchingUserRepository.save(newMatchingUser);
                    }

                    //내가 체크한 매칭에 이미 존재하던 인원 + 내가 매칭한 인원 수가 전체 인원 수와 같다면
                    if(existingPartCount+matchingCount==totalPartCount){
                        //커버를 생성
                        Cover cover=Cover.builder()
                                .title(song.getTitle())
                                .song(song)
                                .build();
                        //커버 저장
                        coverRepository.save(cover);

                        //이번에 매칭 신청한 사람들 데이터 삭제
                        for(UserPartDto userPartDto : coverRegistDto.getUserPartDtos()) {
                            User user = userRepository.findById(userPartDto.getUserPk())
                                    .orElseThrow(() -> new RuntimeException("유저가 존재하지 않음"));
                            Part part = partRepository.findById(userPartDto.getPartPk())
                                    .orElseThrow(() -> new RuntimeException("파트가 존재하지 않음"));
                            CoverUser newCoverUser = CoverUser.builder()
                                    .user(user)
                                    .cover(cover)
                                    .part(part)
                                    .build();
                            //매칭 신청한 사람들을 커버 유저로 옮김
                            coverUserRepository.save(newCoverUser);


                            Optional<MatchingUser> matchingUserDeleteList = matchingUserRepository.findByUserIdAndPartId(userPartDto.getUserPk(), userPartDto.getPartPk());
                            // 해당하는 MatchingUser가 존재하면 삭제
                            if (matchingUserDeleteList.isPresent()) {
                                MatchingUser matchingUser = matchingUserDeleteList.get();
                                matchingUserRepository.delete(matchingUser);
                            }
                        }
                        // 원래 매칭에 존재하던 사람들에 대한 CoverUser 생성 하고 MatchingUser 삭제
                        List<MatchingUser> matchingUsers = matching.getMatchingUsers();
                        for (MatchingUser matchingUser : matchingUsers) {
                            CoverUser newCoverUser = CoverUser.builder()
                                    .user(matchingUser.getUser())
                                    .cover(cover)
                                    .part(matchingUser.getPart())
                                    .build();
                            coverUserRepository.save(newCoverUser); // CoverUser 저장
                            matchingUserRepository.delete(matchingUser); // MatchingUser 삭제
                        }
                        //매칭테이블에서 삭제
                        matchingRepository.delete(matching);
                        Set<Long> users= new HashSet<>();
                        for (MatchingUser matchingUser : matchingUsers) {
                            users.add(matchingUser.getUser().getId());
                        }
                        for (Long user : users) {
                            notificationService.sendNotificationToUser(user, "커버 생성을 시작합니다.");
                        }

//                        fastApiService.fetchDataFromFastAPI("/ai/cover", cover.getId());
                        return Response.of("OK", "2", cover.getId());
                    }else{
                        matching.updatePartCount(existingPartCount+matchingCount);
                        Set<Long> users= new HashSet<>();
                        List<MatchingUser> matchingUsers = matching.getMatchingUsers();
                        for (MatchingUser matchingUser : matchingUsers) {
                            users.add(matchingUser.getUser().getId());
                        }
                        for (Long user : users) {
                            notificationService.sendNotificationToUser(user, "매칭 인원이 추가 되었습니다.");
                        }
                        return Response.of("OK", "매칭 업데이트 완료", 1L);
                    }
                }
            }
        }
        //매칭에 실패했으면 새로 매칭을 만듬
        Matching newMatching=Matching.builder()
                .partCount(matchingCount)
                .song(song)
                .build();
        matchingRepository.save(newMatching);

        Set<Long> users= new HashSet<>();
        for(UserPartDto userPartDto : coverRegistDto.getUserPartDtos()){
            User user = userRepository.findById(userPartDto.getUserPk())
                    .orElseThrow(() -> new RuntimeException("유저가 존재하지 않음"));
            Part part = partRepository.findById(userPartDto.getPartPk())
                    .orElseThrow(() -> new RuntimeException("파트가 존재하지 않음"));
            //매칭중계 테이블에도 데이터 생성하기
            MatchingUser newMatchingUser=MatchingUser.builder()
                    .user(user)
                    .matching(newMatching)
                    .part(part)
                    .build();
            matchingUserRepository.save(newMatchingUser);
            users.add(user.getId());
        }
        for (Long user : users) {
            notificationService.sendNotificationToUser(user, "새로운 매칭이 생성 되었습니다.");
        }
        return Response.of("OK", "새로운 매치 생성 및 매칭 유저 추가", 1L);
    }

    /*
    매주 일요일 0시 0분 0초 주간조회수 리셋
     */
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
    *****************************댓글도 같이 가져오기************************
     */
    public CoverDto getCoverDetail(Long coverPk, Long userPk) throws Exception {
        Cover cover = coverRepository.findById(coverPk)
                .orElseThrow(() -> new Exception("커버 게시글이 존재하지 않음"));
        //user가져오기
        Optional<User> user = userRepository.findById(userPk);
        if (user.isPresent()) {
            Optional<CoverLike> exists = coverLikeRepository.findByCoverAndUser(cover, user.get());
            CoverDto coverDto= CoverDto.convertToDto(cover);
            coverDto.updateExists(exists.isPresent());
        }
        cover.updateCover(cover.getHit()+1,cover.getWeeklyHit()+1);

        //내가 이 게시물을 좋아요 했는지 안했는지 체크하는 부분
        return CoverDto.convertToDto(cover);
    }

    /*
    유저와 게시글이 있는지 확인하고 있다면 해당 값을 댓글에 값을 세팅하고 db에 추가함
    게시글이 존재하는지 체크 -> 유저가 존재하는지 체크 -> 둘다 충족한다면 받아온 내용을 저장하고 저장되는 내용을 반환함
     */
    public void createCoverReview(Long coverPk, String content) throws Exception {
        Cover cover = coverRepository.findById(coverPk)
                .orElseThrow(() -> new Exception("커버 게시글이 존재하지 않음"));

        // 현재 인가에서 유저 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        User user = userRepository.findByUsername(customOAuth2User.getUsername());
        CoverReview coverReview = CoverReview.builder()
                .content(content)
                .cover(cover)
                .user(user)
                .build();

        coverReview = coverReviewRepository.save(coverReview);
        notificationService.sendNotificationToUser(user.getId(),"커버 게시글에 댓글이 작성 되었습니다.");
    }

    /*
    댓글 유무 체크
    댓글 삭제
     */
    public String deleteCoverReview(Long coverReviewPk) throws Exception {
        CoverReview coverReview = coverReviewRepository.findById(coverReviewPk)
                .orElseThrow(() -> new Exception("댓글이 존재하지 않음"));
        coverReviewRepository.delete(coverReview);
        return "댓글 삭제 완료";
    }

    /*
    커버 좋아요
    커버 체크, 유저 체크, 이미 좋아요를 눌렀다면 좋아요 취소
    좋아요를 누르지 않았다면 좋아요 생성
     */
    public CoverLikeDto toggleCoverLike(Long coverPk) throws Exception {
        Cover cover = coverRepository.findById(coverPk)
                .orElseThrow(() -> new Exception("커버 게시글이 존재하지 않음"));

        // 현재 인가에서 유저 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        User user = userRepository.findByUsername(customOAuth2User.getUsername());

        Optional<CoverLike> exists = coverLikeRepository.findByCoverAndUser(cover, user);

        if (exists.isPresent()) {
            // 이미 좋아요를 눌렀다면, 좋아요 삭제
            coverLikeRepository.delete(exists.get());
            int likeCount = cover.getLikeCount() == null ? 0 : cover.getLikeCount() - 1;
            cover.updateLikeCount(likeCount);
            return null;
        } else {
            // 좋아요가 없다면, 새로운 좋아요 생성
            CoverLike like=CoverLike.builder()
                    .cover(cover)
                    .user(user)
                    .build();
            coverLikeRepository.save(like);
            int likeCount = cover.getLikeCount() == null ? 1 : cover.getLikeCount() + 1;
            cover.updateLikeCount(likeCount);
            return CoverLikeDto.convertToDto(like);
        }
    }

    public List<MatchDto> getMatchings() throws Exception {
        //인가에서 user가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
        Long userPk = userRepository.findByUsername(customOAuth2User.getUsername()).getId();

        List<MatchingUser> matchings = matchingUserRepository.findAllByUser(userRepository.findById(userPk)
                .orElseThrow(() -> new Exception("유저가 없습니다.")));

        List<MatchDto> matchDtoList = new ArrayList<>();
        for (MatchingUser matching : matchings) {
            Matching match = matching.getMatching();
            MatchDto matchDto = MatchDto.convertToDto(match);
            matchDtoList.add(matchDto);
        }

        return matchDtoList;
    }

    public void completeCoverVideo(Long coverPk) throws Exception {
        Cover cover = coverRepository.findById(coverPk).orElseThrow(() -> new Exception("커버가 없습니다"));
        List<CoverUser> coverUsers = cover.getCoverUsers();
        Set<Long> users = new HashSet<>();
        for (CoverUser coverUser : coverUsers) {
            Long pk = coverUser.getUser().getId();
            users.add(pk);
        }
        for (Long user : users) {
            User userObject = userRepository.findById(user).orElseThrow(() -> new Exception("유저가 없습니다."));
            notificationService.sendNotificationToUser(userObject.getId(), "커버가 생성 되었습니다.");
        }
    }

    /*
    학습 완료 알림
     */
    public void completeTrain(Long userPk) throws Exception {
        User user = userRepository.findById(userPk).orElseThrow(() -> new Exception("유저가 없습니다."));
        user.updateRole(Role.ROLE_USER);
        userRepository.save(user);

        notificationService.sendNotificationToUser(userPk, "학습이 완료 되었습니다. 커버 기능 이용이 가능합니다.");
    }
}
