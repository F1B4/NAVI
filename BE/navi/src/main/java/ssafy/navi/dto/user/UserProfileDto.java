package ssafy.navi.dto.user;

import lombok.*;
import ssafy.navi.dto.cover.CoverDto;
import ssafy.navi.dto.cover.CoverLikeDto;
import ssafy.navi.dto.noraebang.NoraebangDto;
import ssafy.navi.dto.noraebang.NoraebangLikeDto;
import ssafy.navi.entity.cover.Cover;
import ssafy.navi.entity.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserProfileDto {

    private Long userPk;
    private String nickname;
    private String image;
    private Integer followingCount;
    private Integer followerCount;
    private Boolean isFollow;
    private List<CoverDto> coverDtos;
    private List<NoraebangDto> noraebangDtos;
    private List<CoverLikeDto> coverLikeDtos;
    private List<NoraebangLikeDto> noraebangLikeDtos;
    // 엔티티 Dto로 변환
    public static UserProfileDto convertToDto(User user, Boolean isFollow, List<Cover> covers) {
        UserProfileDto userInfoDto = new UserProfileDto();

        // set
        userInfoDto.setUserPk(user.getId());
        userInfoDto.setNickname(user.getNickname());
        userInfoDto.setImage(user.getImage());
        userInfoDto.setFollowingCount(user.getFollowingCount());
        userInfoDto.setFollowerCount(user.getFollowerCount());
        userInfoDto.setIsFollow(isFollow);
        userInfoDto.setCoverDtos(covers.stream()
                .map(CoverDto::convertToDtoList)
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