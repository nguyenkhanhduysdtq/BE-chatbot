package com.example.demo_chatbot.api;

import com.example.demo_chatbot.dto.document.ApiResponse;
import com.example.demo_chatbot.dto.document.TokenReponseDTO;
import com.example.demo_chatbot.dto.document.TokenRequestDTO;
import com.example.demo_chatbot.dto.document.UserDTO;
import com.example.demo_chatbot.dto.document.authentication.AuthenticationDTO;
import com.example.demo_chatbot.dto.document.authentication.AuthenticationResponseDTO;
import com.example.demo_chatbot.dto.document.introspect.IntrospectRequestDTO;
import com.example.demo_chatbot.dto.document.introspect.IntrospectResponseDTO;
import com.example.demo_chatbot.service.authentication.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationAPI {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponseDTO> login(@RequestBody AuthenticationDTO authenticationDTO) {

        return ApiResponse.<AuthenticationResponseDTO>builder()
                .result(authenticationService.login(authenticationDTO))
                .build();

    }


    @PostMapping("/introspect")
    ApiResponse<IntrospectResponseDTO> introspect(@RequestBody IntrospectRequestDTO introspectRequestDTO) {

        return ApiResponse.<IntrospectResponseDTO>builder()
                .result(authenticationService.introspect(introspectRequestDTO))
                .build();

    }


    @GetMapping("/infor")
    ApiResponse<UserDTO> getInfor(){
        return ApiResponse.<UserDTO>builder()
                .result(authenticationService.getMyInfor())
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<TokenReponseDTO> refreshToken( @RequestBody TokenRequestDTO dto) throws ParseException, JOSEException {
        return ApiResponse.<TokenReponseDTO>builder()
                .result(authenticationService.refreshToken(dto))
                .build();
    }
}
