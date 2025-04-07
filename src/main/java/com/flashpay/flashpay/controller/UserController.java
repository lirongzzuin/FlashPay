package com.flashpay.flashpay.controller;

import com.flashpay.flashpay.domain.User;
import com.flashpay.flashpay.service.UserService;
import com.flashpay.flashpay.util.JwtUtil;
import com.flashpay.flashpay.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.Collections;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 🔐 회원가입
    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    public ResponseEntity<?> register(
            @Parameter(description = "사용자 이름") @RequestParam String username,
            @Parameter(description = "이메일 주소") @RequestParam String email,
            @Parameter(description = "비밀번호") @RequestParam String password) {

        User user = userService.registerUser(username, email, password);
        return ResponseEntity.ok(Collections.singletonMap("message", "회원가입 완료"));
    }

    // 🔐 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 인증 및 JWT 토큰 발급")
    public ResponseEntity<?> login(
            @Parameter(description = "사용자 이름") @RequestParam String username,
            @Parameter(description = "비밀번호") @RequestParam String password) {

        return userService.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    String token = jwtUtil.generateToken(username);
                    return ResponseEntity.ok(Collections.singletonMap("token", token));
                })
                .orElseGet(() -> ResponseEntity.status(401)
                        .body(Collections.singletonMap("error", "로그인 실패")));
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "JWT 토큰을 통해 현재 로그인된 사용자 정보를 조회합니다.")
    public ResponseEntity<?> getMyInfo(@Parameter(description = "JWT 토큰", required = true) @RequestHeader("Authorization") String token) {
        try {
            String username = getUsernameFromToken(token);
            return userService.findByUsername(username)
                    .map(user -> ResponseEntity.ok(ApiResponse.success("프로필 조회 성공", user.getUsername())))
                    .orElse(ResponseEntity.status(404).body(ApiResponse.fail("사용자를 찾을 수 없습니다.", null)));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(ApiResponse.fail("인증 실패: " + e.getMessage(), null));
        }
    }

    private String getUsernameFromToken(String token) {
        String jwt = token.replace("Bearer ", "");
        if (!jwtUtil.validateToken(jwt)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
        return jwtUtil.extractUsername(jwt);
    }
}