package ssafy.navi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssafy.navi.entity.user.User;
import ssafy.navi.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public User findById(Long userPk) throws Exception {
        Optional<User> user = userRepository.findById(userPk);
        return user.orElseThrow(() -> new Exception("유저를 찾을 수 없습니다."));
    }
}
