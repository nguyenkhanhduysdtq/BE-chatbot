package com.example.demo_chatbot.service.infordocument;

import com.example.demo_chatbot.dto.document.InforDocumentDTO;
import com.example.demo_chatbot.dto.document.InforDocumentRequestDTO;
import com.example.demo_chatbot.entity.InforDocument;
import com.example.demo_chatbot.mapper.InForMapper;
import com.example.demo_chatbot.repository.InforDocumentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class InforDocumentService implements IInforDocumentService{

    InforDocumentRepository documentRepository;


    @Override
    public InforDocumentDTO addDocument(InforDocumentRequestDTO dto) {

InforDocument document = InforDocument.builder()
        .nameDocument(dto.getNameDocument())
        .description(dto.getDescription())
        .date(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        .status(0)
        .build();

        return InForMapper.INSTANCE.toDTO(documentRepository.save(document));
    }

    @Override
    public List<InforDocumentDTO> getAll() {

        List<InforDocument> list = documentRepository.findAll();

        List<InforDocumentDTO> result = new ArrayList<>();
        for(InforDocument entity : list){
           result.add(InForMapper.INSTANCE.toDTO(entity));
        }

        return result;
    }

    @Override
    public List<InforDocumentDTO> getByStatus() {

        Optional<List<InforDocument>> list = documentRepository.findByStatus();

        List<InforDocumentDTO> result = new ArrayList<>();
        for(InforDocument entity : list.get()){
            result.add(InForMapper.INSTANCE.toDTO(entity));
        }

        return result;
    }

    @Override
    public InforDocumentDTO update(int id) {
        InforDocument entity = documentRepository.findById(id);

        entity.setStatus(1);


        return InForMapper.INSTANCE.toDTO(documentRepository.save(entity));
    }

    @Override
    public InforDocumentDTO findByDocument(int id) {

        return InForMapper.INSTANCE.toDTO(documentRepository.findById(id));
    }

    @Override
    public boolean deleteDocument(int id) {
        InforDocument document = documentRepository.findById(id);
          if(document != null){
              documentRepository.deleteById(id);

          }else{
             return false;
          }

        return true;
    }

    @Override
    public InforDocumentDTO getDocumentById(int id) {

        InforDocument entity = documentRepository.findById(id);


        return InForMapper.INSTANCE.toDTO(entity);
    }
}
