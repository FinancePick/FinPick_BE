package com.example.finpick.api.controller.user;

import com.example.finpick.api.controller.response.ErrorResponse;
import com.example.finpick.api.controller.user.request.UserRequest;
import com.example.finpick.api.service.user.UserService;
import com.example.finpick.api.service.user.response.UserResponse;
import com.example.finpick.api.service.user.response.UserResponseNoToken;
import com.example.finpick.config.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserRequest request) {
        try {
            UserResponseNoToken userResponseNoToken = userService.signUp(request);
            return ResponseEntity.ok(userResponseNoToken);
        } catch (IllegalArgumentException e) {
            // ErrorResponse로 에러 메시지 전달
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request) {
        UserResponse login = userService.login(request);
        return ResponseEntity.ok(login);
    }

    @GetMapping("/myService")
    public ResponseEntity<UserResponseNoToken> getUserById(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUserId();
        return ResponseEntity.ok(userService.findByID(userId));
    }

    @GetMapping("/user-info")
    public String getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        // CustomUserDetails에서 userId 가져오기
        Long userId = customUserDetails.getUserId();
        return "User ID: " + userId;
    }

}