package techtrek.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techtrek.domain.user.dto.UserResponse;
import techtrek.global.code.CommonResponse;
import techtrek.global.code.status.ResponseCode;
import techtrek.global.exception.GlobalException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/{id}")
    public CommonResponse<UserResponse> getUser(@PathVariable Long id) {
        // 예시 사용자 정보
        UserResponse user = new UserResponse(id, "yuna");
        if (user.getUserId() != 1) {
            throw new GlobalException(ResponseCode.USER_NOT_FOUND);
        }

        return new CommonResponse<>(ResponseCode.SUCCESS, user);
    }
}
