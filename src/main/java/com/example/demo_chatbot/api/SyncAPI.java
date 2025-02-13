package com.example.demo_chatbot.api;

import com.example.demo_chatbot.dto.document.ApiResponse;
import com.example.demo_chatbot.dto.document.DocumentSyncDTO;
import com.example.demo_chatbot.dto.document.DocumentSyncRequestDTO;
import com.example.demo_chatbot.dto.document.UpdateDocumentSyncRequestDTO;
import com.example.demo_chatbot.service.documentSync.ISyncService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SyncAPI {
   ISyncService syncService;

    @PostMapping("/sync/document")
    ApiResponse<DocumentSyncDTO> addDocumentSync(@RequestBody DocumentSyncRequestDTO syncRequestDTO){
        return ApiResponse.<DocumentSyncDTO>builder()
                .result(syncService.addInforDocumentSync(syncRequestDTO))
                .build();
    }

    @PostMapping("/update/documentSync")
    ApiResponse<DocumentSyncDTO> updateDocument(@RequestBody UpdateDocumentSyncRequestDTO syncRequestDTO){

    return ApiResponse.<DocumentSyncDTO>builder()
            .result(syncService.updateDocumentSync(syncRequestDTO))
            .build();
    }

    @GetMapping("/sync/documents")
    ApiResponse<List<DocumentSyncDTO>> getAll(){

        return ApiResponse.<List<DocumentSyncDTO>>builder()
                .result(syncService.getAll())
                .build();
    }


    @GetMapping("/documentSync")
    ApiResponse<DocumentSyncDTO> getSingleDocumentSync(@RequestParam("id") int id){

        return ApiResponse.<DocumentSyncDTO>builder()
                .result(syncService.getDocumentSyncById(id))
                .build();
    }


    @DeleteMapping("/delete/sync")
    ApiResponse<Boolean> deleteSync(@RequestParam("id") int id){

        return ApiResponse.<Boolean>builder()
                .result(syncService.deleteSync(id))
                .build();
    }

    @PutMapping("/update/sync")
    ApiResponse<String> updateSync(@RequestParam("idSync") int idSync, @RequestParam("idInfor") int idInfor,@RequestParam("status") int status){

        return ApiResponse.<String>builder()
                .result(syncService.updateSync(idSync,idInfor,status))
                .build();
    }



}
