package com.example.web_project_1.service;

import com.example.web_project_1.config.JwtTokenProvider;
import com.example.web_project_1.dto.LoginRequest;
import com.example.web_project_1.dto.TokenResponse;
import com.example.web_project_1.model.SessionStatus;
import com.example.web_project_1.model.User;
import com.example.web_project_1.model.UserSession;
import com.example.web_project_1.repository.UserRepository;
import com.example.web_project_1.repository.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserSessionRepository userSessionRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider,
                       UserSessionRepository userSessionRepository,
                       UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userSessionRepository = userSessionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserSession session = new UserSession();
        session.setUser(user);
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusDays(7));
        session.setStatus(SessionStatus.ACTIVE);
        session.setRefreshToken("temp");

        session = userSessionRepository.save(session);

        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRoles(), session.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername(), session.getId());

        session.setRefreshToken(refreshToken);
        userSessionRepository.save(session);

        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional(noRollbackFor = SecurityException.class)
    public TokenResponse refreshToken(String oldRefreshToken) {
        if (!jwtTokenProvider.validateToken(oldRefreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        if (!"REFRESH".equals(jwtTokenProvider.getTokenType(oldRefreshToken))) {
            throw new IllegalArgumentException("Token is not a refresh token");
        }

        Long sessionId = jwtTokenProvider.getSessionId(oldRefreshToken);
        UserSession session = userSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new IllegalArgumentException("Session is not active");
        }

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            session.setStatus(SessionStatus.EXPIRED);
            userSessionRepository.save(session);
            throw new IllegalArgumentException("Session expired");
        }

        if (!session.getRefreshToken().equals(oldRefreshToken)) {
            session.setStatus(SessionStatus.CLOSED);
            userSessionRepository.save(session);
            throw new SecurityException("Refresh token reuse detected. Session closed.");
        }

        User user = session.getUser();
        String newAccessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRoles(), session.getId());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getUsername(), session.getId());

        session.setRefreshToken(newRefreshToken);
        session.setExpiresAt(LocalDateTime.now().plusDays(7));
        userSessionRepository.save(session);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}