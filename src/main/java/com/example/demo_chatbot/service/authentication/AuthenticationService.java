package com.example.demo_chatbot.service.authentication;

import com.example.demo_chatbot.dto.document.TokenReponseDTO;
import com.example.demo_chatbot.dto.document.TokenRequestDTO;
import com.example.demo_chatbot.dto.document.UserDTO;
import com.example.demo_chatbot.dto.document.authentication.AuthenticationDTO;
import com.example.demo_chatbot.dto.document.authentication.AuthenticationResponseDTO;
import com.example.demo_chatbot.dto.document.introspect.IntrospectRequestDTO;
import com.example.demo_chatbot.dto.document.introspect.IntrospectResponseDTO;
import com.example.demo_chatbot.entity.UserEntity;
import com.example.demo_chatbot.enums.TypeToken;
import com.example.demo_chatbot.exception.AppException;
import com.example.demo_chatbot.exception.ErrorResponse;
import com.example.demo_chatbot.mapper.UserMapper;
import com.example.demo_chatbot.repository.TokenRepository;
import com.example.demo_chatbot.repository.UserAccountRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService implements IAuthenticationService {


    UserAccountRepository userAccountRepository;
    PasswordEncoder passwordEncoder;
    TokenRepository tokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    long ACCESS_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    long REFRESH_DURATION;

    @Override
    public AuthenticationResponseDTO login(AuthenticationDTO authenticationDTO) {
        Optional<UserEntity> entity = userAccountRepository.findByUserName(authenticationDTO.getUsername());

        if (entity.isEmpty()) {
            throw new AppException(ErrorResponse.USER_NOT_EXISTED);
        }

        boolean checkPassword = passwordEncoder.matches(authenticationDTO.getPassword(), entity.get().getPassword());


        if (!checkPassword) {
            throw new AppException(ErrorResponse.UNCATEGORIZED_EXCEPTION);
        }

        String token = generateToken(entity.get(), ACCESS_DURATION, String.valueOf(TypeToken.ACCESS_TOKEN));

        String refreshToken = generateToken(entity.get(), REFRESH_DURATION, String.valueOf(TypeToken.REFRESH_TOKEN));
        return AuthenticationResponseDTO.builder()
                .token(token)
                .refreshToken(refreshToken)
                .auth(true)
                .build();
    }

    @Override
    public IntrospectResponseDTO introspect(IntrospectRequestDTO introspectRequestDTO) {

        var token = introspectRequestDTO.getToken();
        boolean isValue = true;

        try {
            SignedJWT signedJWT = verifyToken(token);
        } catch (AppException | JOSEException | ParseException e) {
            isValue = false;
        }


        return IntrospectResponseDTO.builder()
                .result(isValue)
                .build();
    }


    @SuppressWarnings("unused")
    private String generateToken(UserEntity entity, long expirationTime, String typeToken) {

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet;

        if (typeToken.equals(String.valueOf(TypeToken.ACCESS_TOKEN))) {
            jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(entity.getUserName())
                    .issuer(typeToken)
                    .issuer(entity.getFullName())
                    .issueTime(new Date())
                    .expirationTime(
                            new Date(
                                    Instant.now().plus(expirationTime, ChronoUnit.SECONDS).toEpochMilli()
                            )
                    )
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope", scope(entity))
                    .build();

        } else {

            jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(entity.getUserName())
                    .issuer(typeToken)
                    .issueTime(new Date())
                    .expirationTime(
                            new Date(
                                    Instant.now().plus(expirationTime, ChronoUnit.SECONDS).toEpochMilli()
                            )
                    )
                    .jwtID(UUID.randomUUID().toString())
                    .build();
        }

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            return jwsObject.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        return null;


    }


    private String scope(UserEntity entity) {

        StringJoiner stringJoiner = new StringJoiner("");
        String role = entity.getRole() == 1 ? "ADMIN" : "USER";

        stringJoiner.add("ROLE_" + role);


        return stringJoiner.toString();
    }


    private SignedJWT verifyToken(String token) throws JOSEException, ParseException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = com.nimbusds.jwt.SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier); //  reponse true or false

        if (!(verified && expiryTime.after(new Date()))) {
            throw new AppException(ErrorResponse.UNAUTHENTICATED);
        }

        if (tokenRepository.existsByJwtId(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorResponse.UNAUTHENTICATED);
        }

        return signedJWT;

    }


    @Override
    public UserDTO getMyInfor() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        return UserMapper.INSTANCE.toDTO(
                userAccountRepository.findByUserName(name)
                        .orElseThrow(()-> new AppException(ErrorResponse.USER_NOT_EXISTED))
        );
    }

    @Override
    public TokenReponseDTO refreshToken(TokenRequestDTO dto) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(dto.getRefreshToken());

        Optional<UserEntity> entity = userAccountRepository.findByUserName(signedJWT.getJWTClaimsSet().getSubject());

        String token = generateToken(entity.get(), ACCESS_DURATION,TypeToken.ACCESS_TOKEN.toString());

        return TokenReponseDTO.builder()
                .token(token)
                .build();
    }


}
