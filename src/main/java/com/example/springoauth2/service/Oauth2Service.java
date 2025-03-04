package com.example.springoauth2.service;

import com.example.springoauth2.entity.CustomOAuth2User;
import com.example.springoauth2.entity.User;
import com.example.springoauth2.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Oauth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(Oauth2Service.class);

    @Autowired
    public Oauth2Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("login");
        String githubId = oAuth2User.getAttribute("id").toString();

        if (email == null) {

            email = name + "@github.com"; // Временное решение
            logger.warn("Не удалось получить почту,назначаем временную {} .", email);
        }

        String finalEmail = email;
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User(null, githubId, name, finalEmail, finalEmail.equals("admin@example.com") ? "ADMIN" : "USER");
                    logger.info("Пользователь {} сохранен", newUser.getName());
                    return userRepository.save(newUser);
                });
        logger.info("Успешная аутентификация пользователя {} ", name);
        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }
}
