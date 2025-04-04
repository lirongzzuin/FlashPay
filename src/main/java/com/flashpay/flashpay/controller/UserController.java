package com.flashpay.flashpay.controller;

import com.flashpay.flashpay.domain.User;
import com.flashpay.flashpay.service.UserService;
import com.flashpay.flashpay.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 🔐 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {

        User user = userService.registerUser(username, email, password);
        return ResponseEntity.ok(Collections.singletonMap("message", "회원가입 완료"));
    }

    // 🔐 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password) {

        return userService.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    String token = jwtUtil.generateToken(username);
                    return ResponseEntity.ok(Collections.singletonMap("token", token));
                })
                .orElseGet(() -> ResponseEntity.status(401)
                        .body(Collections.singletonMap("error", "로그인 실패")));
    }
}