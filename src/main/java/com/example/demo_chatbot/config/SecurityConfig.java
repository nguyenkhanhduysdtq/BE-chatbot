package com.example.demo_chatbot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS_POST = {
            "/login",
            "/introspect",
            "/logOut",
            "/document",
            "/refresh",
            "/uploads",
            "/infor/update",
            "/file",
            "/sync/document",
            "/update/documentSync",
            "/create",
            "/create/client",
            "/ingest",
            "/chat",
            "/create/description",
            "/ingest/description",
            "/index",
            "/question"

    };

    private final String[] PUBLIC_ENDPOINTS_GET = {
            "/documents",
            "/infor",
            "/getFile",
            "/sync/documents",
            "/documentSync",
            "/v1/document",
            "/v1/documents",
            "/clients",
            "/questions",
            "/export/questions"
    };

    private final String[] PUBLIC_ENDPOINTS_DELETE = {
            "/delete/file",
            "/delete/document",
            "/delete/sync"

    };

    private final String[] PUBLIC_ENDPOINTS_PUT = {
            "/update/sync"

    };


    @Value("${jwt.signerKey}")
    private String signerKey;


    @Autowired
    @Lazy
    CustomJwtDecoder customJwtDecoder;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //Endpoint nào cần han chế và authorized thì thêm vào
        httpSecurity.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS_POST).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS_GET).permitAll()
                        .requestMatchers(HttpMethod.DELETE, PUBLIC_ENDPOINTS_DELETE).permitAll()
                        .requestMatchers(HttpMethod.PUT, PUBLIC_ENDPOINTS_PUT).permitAll()
                        .anyRequest().authenticated()
        );


        //xử lý đăng nhập bằng token
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                                jwtConfigurer.decoder(customJwtDecoder)
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())

        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.accessDeniedHandler(
                        (request, response, accessDeniedException) -> response.setStatus(403)
                ))
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(Customizer.withDefaults())
                .cors((Customizer.withDefaults()
                        )
                );


        return httpSecurity.build();
    }


    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
