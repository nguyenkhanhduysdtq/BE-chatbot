package com.example.demo_chatbot.api;

import com.example.demo_chatbot.dto.document.ApiResponse;
import com.example.demo_chatbot.dto.document.RequestDocument;
import com.example.demo_chatbot.dto.document.ResponseDocument;
import com.example.demo_chatbot.dto.document.ResponseIngestDTO;
import com.example.demo_chatbot.index.DescriptionBasic;
import com.example.demo_chatbot.service.DocumentService;
import com.example.demo_chatbot.service.LLMService;
import groovy.util.logging.Slf4j;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ChatAPI {

    private static final Logger log = LoggerFactory.getLogger(ChatAPI.class);
    DocumentService documentService;
    LLMService llmService;


     @PostMapping("/ingest")
     public ApiResponse<ResponseIngestDTO> ingest(@RequestParam(value = "id") int id){

           ResponseIngestDTO ingestDTO = ResponseIngestDTO.builder()
                   .result(documentService. ingestionAddDB(id))
                   .build();

           return ApiResponse.<ResponseIngestDTO>builder()
                   .result(ingestDTO)
                   .build();
     }

    @PostMapping("/create")
    public ApiResponse<Boolean> createDB(@RequestParam String name){

         log.info("tên file " + name);

        return ApiResponse.<Boolean>builder()
                .result(documentService.createIndex(name))
                .build();
    }


    @PostMapping("/create/description")
    public ApiResponse<Boolean> createDescriptionDB(@RequestParam String name){

        log.info("tên file " + name);

        return ApiResponse.<Boolean>builder()
                .result(documentService.createIndexDescription(name))
                .build();
    }

    @PostMapping("/ingest/description")
    public ApiResponse<ResponseIngestDTO> ingestDescription(@RequestBody DescriptionBasic basic) throws IOException {

        ResponseIngestDTO ingestDTO = ResponseIngestDTO.builder()
                .result(documentService.ingestDectiption(basic))
                .build();

        return ApiResponse.<ResponseIngestDTO>builder()
                .result(ingestDTO)
                .build();
    }



     @PostMapping("/chat")
     public  ApiResponse<ResponseDocument> answer(@RequestBody RequestDocument requestDocument) throws IOException {
         List<Document> resultDocumentSearch = documentService.searchDB(requestDocument).getResultText();


         ResponseDocument answer = ResponseDocument.builder()
                 .answer(llmService.queryDB(requestDocument,resultDocumentSearch).getAnswer())
                 .timeAnswer(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                 .build();

         return ApiResponse.<ResponseDocument>builder()
                 .result(answer)
                 .build();
     }


    @PostMapping("/index")
    public  ApiResponse<String> getIndex(@RequestBody RequestDocument requestDocument) throws IOException {
        String resultDocumentSearch = documentService.searchDescriptionDB(requestDocument);
        return ApiResponse.<String>builder()
                .result(resultDocumentSearch)
                .build();
    }




}
