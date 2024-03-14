package ssafy.navi.dto;

import lombok.*;
import ssafy.navi.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserDto {

    private String nickname;
    private String email;
    private String image;
    private Integer followingCount;
    private Integer followerCount;
//    private List<FollowingDto> followingDtos;
//    private List<FollowerDto> followerDtos;
//    private List<NoraebangDto> noraebangDtos;
//    private List<NoraebangLikeDto> noraebangLikeDtos;
//    private List<NoraebangReviewDto> noraebangReviewDtos;
//    private List<CoverUserDto> coverUserDtos;
//    private List<CoverLikeDto> coverLikeDtos;
//    private List<CoverReviewDto> coverReviewDtos;
//    private List<AlarmDto> alarmDtos;
//    private List<MatchingUserDto> matchingUserDtos;
    
    // 엔티티 Dto로 변환
    public static UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();

        // set
        userDto.setNickname(user.getNickname());
        userDto.setEmail(user.getEmail());
        userDto.setImage(user.getImage());
        userDto.setFollowingCount(user.getFollowingCount());
        userDto.setFollowerCount(user.getFollowerCount());
//        userDto.setFollowingDtos(user.getFollowings()
//                .stream().map(FollowingDto::convertToDto)
//                .collect(Collectors.toList()));
//        userDto.setFollowerDtos(user.getFollowers()
//                .stream().map(FollowerDto::convertToDto)
//                .collect(Collectors.toList()));
//        userDto.setNoraebangDtos(user.getNoraebangs()
//                .stream().map(NoraebangDto::convertToDto)
//                .collect(Collectors.toList()));
//        userDto.setNoraebangLikeDtos(user.getNoraebangLikes()
//                .stream().map(NoraebangLikeDto::convertToDto)
//                .collect(Collectors.toList()));
//        userDto.setNoraebangReviewDtos(user.getNoraebangReviews()
//                .stream().map(NoraebangReviewDto::convertToDto)
//                .collect(Collectors.toList()));
//        userDto.setCoverUserDtos(user.getCoverUsers()
//                .stream().map(CoverUserDto::convertToDto)
//                .collect(Collectors.toList()));
//        userDto.setCoverLikeDtos(user.getCoverLikes()
//                .stream().map(CoverLikeDto::convertToDto)
//                .collect(Collectors.toList()));
//        userDto.setCoverReviewDtos(user.getCoverReviews()
//                .stream().map(CoverReviewDto::convertToDto)
//                .collect(Collectors.toList()));
//        userDto.setAlarmDtos(user.getAlarms()
//                .stream().map(AlarmDto::convertToDto)
//                .collect(Collectors.toList()));
//        userDto.setMatchingUserDtos(user.getMatchingUsers()
//                .stream().map(MatchingUserDto::convertToDto)
//                .collect(Collectors.toList()));

        return userDto;
    }
    public static UserDto convertToDtoCoverDetail(User user){
        UserDto userDto = new UserDto();
        // set
        userDto.setNickname(user.getNickname());
        userDto.setImage(user.getImage());
        return userDto;
    }
}
