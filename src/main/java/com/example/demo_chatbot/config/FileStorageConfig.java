package com.example.demo_chatbot.config;

import org.springframework.stereotype.Component;

@Component
public class FileStorageConfig {


    public String getUploadDir() {
        // Trỏ tới thư mục "uploads" trong thư mục gốc của project
        String projectDir = System.getProperty("user.dir"); // Thư mục gốc của project
        String uploadDir = projectDir + "/demo-chatbot/uploads/"; // Thư mục "uploads" trong project
        return uploadDir;
    }
}
