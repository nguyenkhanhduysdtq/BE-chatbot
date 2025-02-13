package com.example.demo_chatbot.service.documentSync;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import com.example.demo_chatbot.dto.document.DocumentSyncDTO;
import com.example.demo_chatbot.dto.document.DocumentSyncRequestDTO;
import com.example.demo_chatbot.dto.document.UpdateDocumentSyncRequestDTO;
import com.example.demo_chatbot.entity.DocumentSync;
import com.example.demo_chatbot.entity.InforDocument;
import com.example.demo_chatbot.mapper.DocumentSyncMapper;
import com.example.demo_chatbot.repository.DocumentSyncRepository;
import com.example.demo_chatbot.repository.InforDocumentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SyncService implements ISyncService {
    private static final Logger log = LoggerFactory.getLogger(SyncService.class);
    DocumentSyncRepository repository;
    InforDocumentRepository documentRepository;
    ElasticsearchClient elasticsearchClient;


    @Override
    public DocumentSyncDTO addInforDocumentSync(DocumentSyncRequestDTO syncRequestDTO) {

        if (syncRequestDTO == null) {
            throw new IllegalArgumentException("Request DTO cannot be null");
        }

        if (syncRequestDTO.getNameDocument() == null || syncRequestDTO.getNameDocument().isEmpty()) {
            throw new IllegalArgumentException("NameDocument cannot be null or empty");
        }

        if (syncRequestDTO.getDescription() == null || syncRequestDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }

        DocumentSync entity = DocumentSync.builder()
                .nameDocument(syncRequestDTO.getNameDocument())
                .description(syncRequestDTO.getDescription())
                .date(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .status(0)
                .collectionDB("store_"+UUID.randomUUID().toString())
                .build();

        return DocumentSyncMapper.INSTANCE.toDTO(repository.save(entity));
    }

    @Override
    public DocumentSyncDTO updateDocumentSync(UpdateDocumentSyncRequestDTO dto) {

        InforDocument document =documentRepository.findById(dto.getIdInfor());

        Optional<DocumentSync> documentSync = repository.findById(dto.getIdDocumentSync());

        documentSync.get().setStatus(1);
        documentSync.get().setDocument(document);

        return DocumentSyncMapper.INSTANCE.toDTO(repository.save(documentSync.get()));
    }

    @Override
    public List<DocumentSyncDTO> getAll() {

        List<DocumentSync> entity = repository.findAll();

        List<DocumentSyncDTO> result = new ArrayList<>();

        for (DocumentSync rowEntity : entity){
            result.add(DocumentSyncMapper.INSTANCE.toDTO(rowEntity));
        }
        return result;
    }

    @Override
    public DocumentSyncDTO getDocumentSyncById(int id) {

        Optional<DocumentSync> entity = repository.findById(id);

        DocumentSyncDTO documentDTO = DocumentSyncMapper.INSTANCE.toDTO(entity.get());

        if(entity.get().getDocument() == null){
            documentDTO.setInforId(0);
        }else {
            documentDTO.setInforId(entity.get().getDocument().getId());

        }

        return documentDTO;
    }

    @Override
    @Transactional
    public boolean deleteSync(int id) {
        Optional<DocumentSync> entity = repository.findById(id);

        if(entity.isEmpty()) {
            return false;

        }

        final String INDEX_NAME_DESCRIPTION = "description_store_6c982379-4b94-4892-b948-57cf9997e9c1";

        try{

            repository.deleteById(id);

            DeleteIndexResponse deleteIndex = elasticsearchClient.indices().delete(d -> d
                        .index(entity.get().getCollectionDB()));

            DeleteByQueryResponse response = elasticsearchClient.deleteByQuery(d -> d
                    .index(INDEX_NAME_DESCRIPTION)
                    .query(q -> q
                            .match(m -> m // Dùng match thay vì term
                                    .field("store")
                                    .query(entity.get().getCollectionDB())
                            )
                    )
            );




        }catch (Exception e){
             e.getMessage();
        }


        return true;
    }

    @Override
    public String updateSync(int idSync, int idInfor ,int status) {

        Optional<DocumentSync> documentSyncEntity = repository.findById(idSync);

        documentSyncEntity.get().setStatus(status);

        if( idInfor != -1){
            InforDocument inforDocumentEntity =documentRepository.findById(idInfor);
            documentSyncEntity.get().setDocument(inforDocumentEntity);

            repository.save(documentSyncEntity.get());

            return inforDocumentEntity.getNameDocument();
        }



           repository.save(documentSyncEntity.get());

           return "update status";
    }
}
