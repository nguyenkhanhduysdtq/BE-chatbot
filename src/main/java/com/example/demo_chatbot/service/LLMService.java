package com.example.demo_chatbot.service;


import com.example.demo_chatbot.dto.document.RequestDocument;
import com.example.demo_chatbot.dto.document.ResponseDocument;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
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

    public ResponseDocument queryDB(RequestDocument requestDocument, List<Document> document){



        String documents = document.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n"));

        String prompt =  """
                            Bạn là một trợ lý hữu ích .
                            Bạn rất thông minh luôn đưa ra câu trả lời đúng.
                            Chỉ sử dụng những thông tin theo như trong tài liệu cung cấp để trả lời câu hỏi.
                            Không được sử dụng bất kỳ những thông tin khác. Nếu không biết,đơn gian trả lời: Xin lỗi thông tin liên quan không có trong tài liệu.
                            Không trả lời thông tin quá dài.
                            
                             
                             câu hỏi:
                             {input}
                             
                             tài liệu:
                             {documents}
                        """;

        PromptTemplate template
                = new PromptTemplate(prompt);
        Map<String, Object> promptsParameters = new HashMap<>();
        promptsParameters.put("input", requestDocument.getQuestion());
        promptsParameters.put("documents", documents);


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
