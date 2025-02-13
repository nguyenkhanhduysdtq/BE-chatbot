package com.example.demo_chatbot.api;

import com.example.demo_chatbot.config.FileStorageConfig;
import com.example.demo_chatbot.dto.document.ApiResponse;
import com.example.demo_chatbot.dto.document.FileDTO;
import com.example.demo_chatbot.dto.document.InforDocumentDTO;
import com.example.demo_chatbot.dto.document.InforDocumentRequestDTO;
import com.example.demo_chatbot.exception.AppException;
import com.example.demo_chatbot.exception.ErrorResponse;
import com.example.demo_chatbot.service.file.IFileService;
import com.example.demo_chatbot.service.infordocument.IInforDocumentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InforDocumentAPI {

    IInforDocumentService documentService;
    FileStorageConfig fileStorageConfig;
    IFileService service;


    @PostMapping("/document")
    ApiResponse<InforDocumentDTO> addDocument(@RequestBody InforDocumentRequestDTO dto){

        return ApiResponse.<InforDocumentDTO>builder()
                .result(documentService.addDocument(dto))
                .build();

    }


    @GetMapping("/documents")
    ApiResponse<List<InforDocumentDTO>> getAll(){

        return ApiResponse.<List<InforDocumentDTO>>builder()
                .result(documentService.getAll())
                .build();

    }

    @GetMapping("/v1/documents")
    ApiResponse<List<InforDocumentDTO>> getByStatus(){

        return ApiResponse.<List<InforDocumentDTO>>builder()
                .result(documentService.getByStatus())
                .build();

    }


    @PostMapping("/uploads")
    ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File không hợp lệ.");
        }

        // Lấy đường dẫn thư mục lưu file
        String uploadDir = fileStorageConfig.getUploadDir();
        File directory = new File(uploadDir);

        // Tạo thư mục nếu chưa tồn tại
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            // Tạo file trong thư mục
            File serverFile = new File(uploadDir + file.getOriginalFilename());

            // Lưu file vào thư mục
            try (FileOutputStream stream = new FileOutputStream(serverFile)) {
                stream.write(file.getBytes());
            }

            return ResponseEntity.ok("File đã được upload thành công: " + serverFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi khi lưu file.");
        }
    }


    @PostMapping("/infor/update")
    ApiResponse<InforDocumentDTO> update(@RequestParam("id") int id){

        return ApiResponse.<InforDocumentDTO>builder()
                .result(documentService.update(id))
                .build();
    }

    @DeleteMapping("/delete/document")
    ApiResponse<Boolean> deleteDocument(@RequestParam("id") int id){

        return  ApiResponse.<Boolean>builder()
                .result(documentService.deleteDocument(id))
                .build();
    }


    @GetMapping("/v1/document")
    ApiResponse<InforDocumentDTO> getDocumetByid(@RequestParam("id") int id){

        return ApiResponse.<InforDocumentDTO>builder()
                .result(documentService.getDocumentById(id))
                .build();
    }




}
