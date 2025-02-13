package com.example.demo_chatbot.service.documentSync;

import com.example.demo_chatbot.dto.document.DocumentSyncDTO;
import com.example.demo_chatbot.dto.document.DocumentSyncRequestDTO;
import com.example.demo_chatbot.dto.document.UpdateDocumentSyncRequestDTO;
import com.example.demo_chatbot.entity.DocumentSync;

import java.util.List;

public interface ISyncService {
    DocumentSyncDTO addInforDocumentSync(DocumentSyncRequestDTO syncRequestDTO);
    DocumentSyncDTO updateDocumentSync(UpdateDocumentSyncRequestDTO dto);
    List<DocumentSyncDTO> getAll();
    DocumentSyncDTO getDocumentSyncById(int id);
    boolean deleteSync(int id);
    String updateSync(int idSync , int idInfor,int status);
}
