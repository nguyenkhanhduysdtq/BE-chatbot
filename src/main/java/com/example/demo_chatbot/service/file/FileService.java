package com.example.demo_chatbot.service.file;

import com.example.demo_chatbot.config.FileStorageConfig;
import com.example.demo_chatbot.dto.document.FileDTO;
import com.example.demo_chatbot.dto.document.FileRequestDTO;
import com.example.demo_chatbot.entity.FileEntity;
import com.example.demo_chatbot.entity.InforDocument;
import com.example.demo_chatbot.exception.AppException;
import com.example.demo_chatbot.exception.ErrorResponse;
import com.example.demo_chatbot.mapper.FileMapper;
import com.example.demo_chatbot.repository.FileRepository;
import com.example.demo_chatbot.repository.InforDocumentRepository;
import groovy.util.logging.Slf4j;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class FileService implements  IFileService{

    private static final Logger log = LoggerFactory.getLogger(FileService.class);
    FileRepository repository;
      InforDocumentRepository documentRepository;
      FileStorageConfig fileStorageConfig;


      @Override
      public FileDTO saveFile(FileRequestDTO dto) {
            FileEntity entity = FileMapper.INSTANCE.toEntity(FileDTO.builder()
                            .fileName(dto.getFileName())
                    .build());
            InforDocument document = documentRepository.findById(dto.getInforId());

            entity.setInfor(document);


            return FileMapper.INSTANCE.toDTO(repository.save(entity));
      }

      @Override
      public FileDTO getFindId(int id) {
        InforDocument inforDocument = documentRepository.findById(id);

            if (inforDocument.getFile() == null) {
                  throw new AppException(ErrorResponse.FILE_NOT_ACCEPT);
            }

            return FileMapper.INSTANCE.toDTO(inforDocument.getFile());
      }

    @Override
    public boolean deleteFile(FileDTO dto) {

        try {
            Path filePath = Paths.get(fileStorageConfig.getUploadDir()).resolve(dto.getFileName()).normalize(); // Đường dẫn đầy đủ tới tệp

            File file = filePath.toFile();
            if(file.exists() && file.isFile()) {

                if (repository.existsById(dto.getId())) {


                    //Update status information document
                    Optional<FileEntity> entity = repository.findById(dto.getId());

                    InforDocument document = documentRepository.findById(entity.get().getInfor().getId());

                    document.setStatus(0);

                    documentRepository.save(document);

                    file.delete(); // delete file in the project
                    repository.deleteById(dto.getId()); // delete file database


                } else {
                    return false;
                }

            }
        } catch (Exception e) {

            return false;
        }

        return  true;
    }
}
