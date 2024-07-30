package gift.user.presentation;

import gift.user.application.UserService;
import gift.user.domain.NormalUserRegisterRequest;
import gift.user.domain.User;
import gift.user.domain.UserRegisterRequest;
import gift.util.CommonResponse;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserController", description = "유저 관련 API")
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "일반 유저 등록(kakao x)", description = "새로운 유저를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody NormalUserRegisterRequest request) {
        User user = userService.registerUser(request);
        String tokenResponse = jwtUtil.generateToken(user, 60L);
        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(summary = "유저 로그인", description = "유저 로그인을 처리합니다.")
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) {
        String tokenResponse = jwtUtil.generateToken(user, 60L);
        return ResponseEntity.ok(new AuthenticationResponse(tokenResponse, user.getEmail()));
    }

    @Operation(summary = "유저 정보 조회", description = "유저 정보를 조회합니다.")
    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo() {
        return ResponseEntity.ok(new CommonResponse<>(
                userService.getUsers(),
                "유저 정보 조회 성공",
                true
        ));
    }

    public static class AuthenticationResponse {

        private final String token;
        private final String email;

        public AuthenticationResponse(String token, String email) {
            this.token = token;
            this.email = email;
        }
    }
}
