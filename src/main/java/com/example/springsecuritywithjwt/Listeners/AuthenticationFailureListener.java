package com.example.springsecuritywithjwt.Listeners;

import com.example.springsecuritywithjwt.entity.User;
import com.example.springsecuritywithjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationFailureListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = (String) event.getAuthentication().getPrincipal();
        if (username == null || username.isEmpty()) {
            return;
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (user != null && user.isAccountNonLocked()) {
            if (user.getFailedLoginAttempts() < MAX_FAILED_ATTEMPTS - 1) {
                user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
                userRepository.save(user);
            } else {
                user.setIsAccountNonLocked(false);
                user.setFailedLoginAttempts(MAX_FAILED_ATTEMPTS);
                userRepository.save(user);
            }
        }
    }
}
