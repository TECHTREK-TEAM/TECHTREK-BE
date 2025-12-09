//package techtrek.global.securty.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import techtrek.domain.user.entity.User;
//import techtrek.domain.user.repository.UserRepository;
//import techtrek.global.common.code.ErrorCode;
//import techtrek.global.common.exception.CustomException;
//
//@Component
//@RequiredArgsConstructor
//public class UserValidator {
//
//    private final UserRepository userRepository;
//
//    /**
//     * userId로 사용자 조회 후 존재하지 않으면 USER_NOT_FOUND 예외 발생
//     */
//    public User validateAndGetUser(String userId) {
//        return userRepository.findById(userId)
//                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
//    }
//
//}
