package com.example.demo_chatbot.service.authentication;

import com.example.demo_chatbot.dto.document.TokenReponseDTO;
import com.example.demo_chatbot.dto.document.TokenRequestDTO;
import com.example.demo_chatbot.dto.document.UserDTO;
import com.example.demo_chatbot.dto.document.authentication.AuthenticationDTO;
import com.example.demo_chatbot.dto.document.authentication.AuthenticationResponseDTO;
import com.example.demo_chatbot.dto.document.introspect.IntrospectRequestDTO;
import com.example.demo_chatbot.dto.document.introspect.IntrospectResponseDTO;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResponseDTO login(AuthenticationDTO authenticationDTO);

    IntrospectResponseDTO introspect(IntrospectRequestDTO introspectRequestDTO);
    UserDTO getMyInfor();
    TokenReponseDTO refreshToken(TokenRequestDTO dto) throws ParseException, JOSEException;



}
