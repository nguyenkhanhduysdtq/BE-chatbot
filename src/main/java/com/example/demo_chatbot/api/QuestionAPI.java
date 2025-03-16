package com.example.demo_chatbot.api;

import com.example.demo_chatbot.dto.document.ApiResponse;
import com.example.demo_chatbot.dto.document.QuestionDTO;
import com.example.demo_chatbot.dto.document.QuestionRequestDTO;
import com.example.demo_chatbot.service.question.InforQuestionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class QuestionAPI {

    InforQuestionService questionService;


    @PostMapping("/question")
    ApiResponse<QuestionDTO> addQuestion(@RequestBody QuestionRequestDTO dto){

        return ApiResponse.<QuestionDTO>builder()
                .result(questionService.addQuestion(dto))
                .build();
    }


    @GetMapping("/questions")
    ApiResponse<List<QuestionDTO>> addQuestion(@RequestParam("id") String id){

        return ApiResponse.<List<QuestionDTO>>builder()
                .result(questionService.getQuestionByIdClient(id))
                .build();
    }

    @GetMapping("/export/questions")
    ApiResponse<Void> exportExcel() throws IOException {
         questionService.exportExcel();

        return ApiResponse.<Void>builder().build();
    }




}
