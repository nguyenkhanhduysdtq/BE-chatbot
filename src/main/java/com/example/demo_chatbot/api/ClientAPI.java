package com.example.demo_chatbot.api;


import com.example.demo_chatbot.dto.document.ApiResponse;
import com.example.demo_chatbot.dto.document.ClientDTO;
import com.example.demo_chatbot.dto.document.ClientRequestDTO;
import com.example.demo_chatbot.service.client.ClientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ClientAPI {

    ClientService clientService;


    @PostMapping("/create/client")
    ApiResponse<ClientDTO> createClient(@RequestBody ClientRequestDTO dto){

        return ApiResponse.<ClientDTO>builder()
                .result(clientService.createClient(dto))
                .build();
    }

}
