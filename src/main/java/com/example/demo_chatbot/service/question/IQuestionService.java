package com.example.demo_chatbot.service.question;

import com.example.demo_chatbot.dto.document.QuestionDTO;
import com.example.demo_chatbot.dto.document.QuestionRequestDTO;

import java.io.IOException;
import java.util.List;

public interface IQuestionService {
    QuestionDTO addQuestion(QuestionRequestDTO dto);
    List<QuestionDTO> getQuestionByIdClient(String id);
    void exportExcel() throws IOException;
    List<QuestionDTO> getQuestionUserbyId(String userId);
}
