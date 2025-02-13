package com.example.demo_chatbot.api;

import com.example.demo_chatbot.dto.document.ApiResponse;
import com.example.demo_chatbot.dto.document.FileDTO;
import com.example.demo_chatbot.dto.document.FileRequestDTO;
import com.example.demo_chatbot.service.file.IFileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
public class FileAPI {

    IFileService service;


    @PostMapping("/file")
    ApiResponse<FileDTO> saveFile(@RequestBody FileRequestDTO dto){


        return ApiResponse.<FileDTO>builder()
                .result(service.saveFile(dto))
                .build();
    }

    @GetMapping("/getFile")
    ApiResponse<FileDTO> getFile(@RequestParam(value = "id") int id){


        return ApiResponse.<FileDTO>builder()
                .result(service.getFindId(id))
                .build();
    }

    @DeleteMapping("/delete/file")
    ApiResponse<Boolean> deleteFile(@RequestBody FileDTO dto){

        return  ApiResponse.<Boolean>builder()
                .result(service.deleteFile(dto))
                .build();
    }

}
