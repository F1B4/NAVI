package ssafy.navi.dto.user;

import lombok.*;
import ssafy.navi.dto.cover.CoverLikeDto;
import ssafy.navi.dto.cover.CoverUserDto;
import ssafy.navi.dto.noraebang.NoraebangDto;
import ssafy.navi.dto.noraebang.NoraebangLikeDto;
import ssafy.navi.entity.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserInfoDto {

    private String nickname;
    private String image;
    private Integer followingCount;
    private Integer followerCount;
    private List<CoverUserDto> coverUserDtos;
    private List<NoraebangDto> noraebangDtos;
    private List<CoverLikeDto> coverLikeDtos;
    private List<NoraebangLikeDto> noraebangLikeDtos;
    // 엔티티 Dto로 변환
    public static UserInfoDto convertToDto(User user) {
        UserInfoDto userInfoDto = new UserInfoDto();

        // set
        userInfoDto.setNickname(user.getNickname());
        userInfoDto.setImage(user.getImage());
        userInfoDto.setFollowingCount(user.getFollowingCount());
        userInfoDto.setFollowerCount(user.getFollowerCount());
        userInfoDto.setCoverUserDtos(user.getCoverUsers().stream()
                .map(CoverUserDto::convertToDto)
                .collect(Collectors.toList()));
        userInfoDto.setNoraebangDtos(user.getNoraebangs().stream()
                .map(NoraebangDto::convertToDtoNoraebangs)
                .collect(Collectors.toList()));
        userInfoDto.setCoverLikeDtos(user.getCoverLikes().stream()
                .map(CoverLikeDto::convertToDto)
                .collect(Collectors.toList()));
        userInfoDto.setNoraebangLikeDtos(user.getNoraebangLikes().stream()
                .map(NoraebangLikeDto::convertToDto)
                .collect(Collectors.toList()));
        return userInfoDto;
    }
}
