package com.example.demo_chatbot.config;

import com.example.demo_chatbot.entity.UserEntity;
import com.example.demo_chatbot.repository.UserAccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;



@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserAccountRepository userAccountRepository) {

        return args -> {
            if (userAccountRepository.findByUserName("admin").isEmpty()) {

                UserEntity admin = UserEntity.builder()
                        .userName("admin")
                        .password(passwordEncoder.encode("123"))
                        .name("Duy")
                        .fullName("Nguyễn Khánh Duy")
                        .role(1)
                        .build();


                userAccountRepository.save(admin);
                log.warn("admin user has been crated with default password : admin , please change it");
            }
        };
    }
}
