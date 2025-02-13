package com.example.demo_chatbot.service.infordocument;

import com.example.demo_chatbot.dto.document.InforDocumentDTO;
import com.example.demo_chatbot.dto.document.InforDocumentRequestDTO;

import java.util.List;

public interface IInforDocumentService {
    InforDocumentDTO addDocument(InforDocumentRequestDTO dto);
    List<InforDocumentDTO> getAll();
    InforDocumentDTO update(int id);
    InforDocumentDTO findByDocument(int id);
    boolean deleteDocument(int id);
    InforDocumentDTO getDocumentById(int id);
    List<InforDocumentDTO> getByStatus();
}
