package com.example.demo_chatbot.service;


import com.example.demo_chatbot.dto.document.QuestionDTO;
import com.example.demo_chatbot.dto.document.RequestDocument;
import com.example.demo_chatbot.dto.document.ResponseDocument;
import com.example.demo_chatbot.service.question.IQuestionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class LLMService {

   ChatModel chatModel;

   IQuestionService questionService;

    public ResponseDocument queryDB(RequestDocument requestDocument, List<Document> document){


        List<QuestionDTO> history = questionService.getQuestionUserbyId(requestDocument.getUserId());

        String documents = document.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n"));

        String chatHistory = history.stream()
                .limit(3)
                .map(QuestionDTO::getQuestion)
                .collect(Collectors.joining("\n"));

        String prompt =   """
                              Bạn là nhân viên tư vấn tuyển sinh của trường Đại học Sư Phạm Hà Nội.
                              Trả lời chính xác, ngắn gọn, dễ hiểu, tránh dư thừa.
                              Chỉ sử dụng thông tin từ tài liệu bên dưới.

                              Nếu nội dung không liên quan đến trường, trả lời:
                              "Xin lỗi, tôi không có thông tin về vấn đề này."
                              
                              Câu trả lời phải xuống dòng hợp lý, định dạng rõ ràng.
                              Sử dụng danh sách (- hoặc số) nếu cần.

                              ### Lịch sử cuộc trò chuyện:
                              {chat_history}

                              ### Câu hỏi của người dùng:
                              {input}

                              ### Tài liệu liên quan:
                              {documents}
                              """;

        PromptTemplate template
                = new PromptTemplate(prompt);
        Map<String, Object> promptsParameters = new HashMap<>();
        promptsParameters.put("input", requestDocument.getQuestion());
        promptsParameters.put("documents", documents);
        promptsParameters.put("chat_history", chatHistory);


        // Gửi truy vấn đến OpenAI và xử lý kết quả
        String result;
        try {
            result = chatModel
                    .call(template.create(promptsParameters))
                    .getResult()
                    .getOutput()
                    .getText();
        } catch (Exception e) {
            result = "Xin lỗi, hệ thống không thể xử lý yêu cầu của bạn vào lúc này.";
        }

        return ResponseDocument.builder()
                .answer(result)
                .timeAnswer(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }




}
