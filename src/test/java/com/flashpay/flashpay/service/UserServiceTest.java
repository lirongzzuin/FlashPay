package com.flashpay.flashpay.service;

import com.flashpay.flashpay.domain.User;
import com.flashpay.flashpay.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void 회원가입_정상등록_확인() {
        String username = "testuser";
        String email = "test@example.com";
        String password = "securepass";

        User expectedUser = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        when(userRepository.save(Mockito.any(User.class))).thenReturn(expectedUser);

        User saved = userService.registerUser(username, email, password);

        assertThat(saved.getUsername()).isEqualTo(username);
        assertThat(saved.getEmail()).isEqualTo(email);
        assertThat(saved.getPassword()).isEqualTo(password);
        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    void 사용자명으로_사용자조회() {
        String username = "sampleUser";
        User user = User.builder().username(username).build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername(username);

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(username);
    }
}
