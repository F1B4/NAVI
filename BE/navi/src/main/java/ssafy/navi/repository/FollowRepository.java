package ssafy.navi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ssafy.navi.dto.user.UserDto;
import ssafy.navi.entity.user.Follow;
import ssafy.navi.entity.user.User;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    @Query("SELECT f1.fromUser FROM Follow f1 JOIN Follow f2 ON f1.fromUser = f2.toUser AND f2.fromUser = f1.toUser WHERE f1.toUser.id = :userId")
    List<User> findMutualFollowers(@Param("userId") Long userId);

    @Query("SELECT f1.fromUser FROM Follow f1 " +
            "JOIN Follow f2 ON f1.fromUser = f2.toUser AND f2.fromUser = f1.toUser " +
            "WHERE f1.toUser.id = :userId AND f1.fromUser.nickname LIKE %:keyword%")
    List<User> findMutualFollowersByKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);

    Follow findByFromUserIdAndToUserId(Long fromUserPk, Long toUserPk);

    List<Follow> findAllByFromUserId(Long fromUserPk);

    List<Follow> findAllByToUserId(Long toUesrPk);
}
