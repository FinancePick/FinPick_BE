package com.example.finpick.api.service.user;

import com.example.finpick.api.controller.user.request.UserRequest;
import com.example.finpick.api.service.user.response.UserResponse;
import com.example.finpick.api.service.user.response.UserResponseNoToken;
import com.example.finpick.domain.user.User;
import com.example.finpick.domain.user.UserRepository;
import com.example.finpick.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserResponseNoToken signUp(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole("User");
        user.setLevel("beginner"); // 기본 레벨 추가

        return entityToSignDto(userRepository.save(user));
    }

    public UserResponse login(UserRequest userRequest) {
        User user = userRepository.findByUsername(userRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername(), user.getLevel()); // 토큰에 레벨 포함
            return new UserResponse(user.getId(), user.getUsername(), token);
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public UserResponseNoToken findByID(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return entityToSignDto(user);
    }


    private UserResponseNoToken entityToSignDto(User user) {
        return new UserResponseNoToken(user.getId(), user.getUsername());
    }

    public void changePassword(Long userId, UserRequest.ChangePasswordRequest request) {
        // 사용자 정보 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        // 새 비밀번호 설정
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void deleteAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }
}
