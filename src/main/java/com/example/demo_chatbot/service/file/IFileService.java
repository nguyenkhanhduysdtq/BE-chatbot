package com.example.demo_chatbot.service.file;


import com.example.demo_chatbot.dto.document.FileDTO;
import com.example.demo_chatbot.dto.document.FileRequestDTO;

public interface IFileService {

    FileDTO saveFile(FileRequestDTO dto);
    FileDTO getFindId(int id);
    boolean deleteFile(FileDTO dto );

}
