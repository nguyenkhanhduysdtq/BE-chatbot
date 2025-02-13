package com.example.demo_chatbot.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.JsonData;
import com.example.demo_chatbot.dto.document.RequestDocument;
import com.example.demo_chatbot.dto.document.SearchDBResponse;
import com.example.demo_chatbot.entity.DocumentSync;
import com.example.demo_chatbot.index.DescriptionBasic;
import com.example.demo_chatbot.index.DocumentBasic;
import com.example.demo_chatbot.index.DocumentStore;
import com.example.demo_chatbot.repository.DocumentSyncRepository;
import com.example.demo_chatbot.repository.FileRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.elasticsearch.ElasticsearchVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class DocumentService {


    @Value("${spring.ai.openai.api-key}")
    String OPENAI_API_KEY ;


    private final ElasticsearchOperations elasticsearchOperations;

    private final ElasticsearchClient elasticsearchClient;

    private  ElasticsearchVectorStore vectorStore;

    private  ChatClient chatClient;

    private final DocumentSyncRepository  repository;

    private final FileRepository fileRepository;

    private final EmbeddingModel embeddingModel;

    public DocumentService(ElasticsearchVectorStore vectorStore, ChatClient.Builder clientBuilder ,ElasticsearchOperations elasticsearchOperations ,EmbeddingModel embeddingModel
    ,ElasticsearchClient elasticsearchClient , DocumentSyncRepository  repository , FileRepository fileRepository) {
        this.vectorStore = vectorStore;
        this.chatClient = clientBuilder.build();
        this.elasticsearchOperations = elasticsearchOperations;
        this.embeddingModel = embeddingModel;
        this.elasticsearchClient = elasticsearchClient;
        this.repository = repository;
        this.fileRepository = fileRepository;
    }


    //get file
    public String getFilePath(String fileName) {
        // Lấy đường dẫn tuyệt đối của thư mục gốc của project
        String projectRoot = System.getProperty("user.dir");

        // Ghép đường dẫn đến thư mục uploads
        File file = new File(projectRoot + File.separator + "uploads" + File.separator + fileName);

        if (!file.exists()) {
            throw new RuntimeException("File không tồn tại: " + file.getAbsolutePath());
        }

        return file.getAbsolutePath();
    }

    @Transactional
    public boolean ingestionAddDB(int id){

        Optional<DocumentSync> documentSync = repository.findById(id);

        //get name file in table file
        String nameFile = fileRepository.getNameFile(documentSync.get().getDocument().getId());


        if(nameFile == null || nameFile.equals(" ")){
            return false;
        }


        String pathDocumentFinal = getFilePath(nameFile);



        try{


            List<Document> docbatch = new ArrayList<>();

            try (PdfReader reader = new PdfReader(pathDocumentFinal);
                 PdfDocument pdfDoc = new PdfDocument(reader)) {

                StringBuilder extractedText = new StringBuilder();

                for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                    extractedText.append(PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i)));
                }

                // Chuyển thành danh sách Document
                docbatch.add(new Document(extractedText.toString()));
            }

            docbatch = new TokenTextSplitter().apply(docbatch);

            // Kiểm tra index có tồn tại không trước khi ghi dữ liệu
            if (!vectorStore.indexExists()) {
                log.error("Index 'my_vector_index' not found! Please create the index first.");
                return false;
            }

            log.info(String.valueOf(docbatch.size()));
            for (Document doc : docbatch) {

                List<float[]> embeddings = embeddingModel.embed(Collections.singletonList(doc.getText()));

                if (embeddings.isEmpty() || embeddings.get(0).length != 1024) {
                    log.error(" Embedding không hợp lệ! Số chiều: " + (embeddings.isEmpty() ? "0" : embeddings.get(0).length));
                    continue;
                }

                 log.info(doc.getText());
                 log.info(Arrays.toString(embeddings.get(0)));


                DocumentBasic myIndex = DocumentBasic.builder()

                        .id(UUID.randomUUID().toString())
                        .content(doc.getText())
                        .embedding(embeddings.get(0))
                        .build();


                // method custom save
                IndexResponse response = elasticsearchClient.index(i -> i
                        .index(documentSync.get().getCollectionDB())
                        .id(myIndex.getId())
                        .document(myIndex)
                );

                IndexResponse responseToal = elasticsearchClient.index(i -> i
                        .index("document-total-11-01-03")
                        .id(myIndex.getId())
                        .document(myIndex)
                );

            }




        }catch (Exception e ){
            log.info(e.getMessage());

            return  false;
        }


        return true;
    }

    @Transactional
    public boolean ingestDectiption(DescriptionBasic descriptionBasic) throws IOException {



            // Kiểm tra index có tồn tại không trước khi ghi dữ liệu
            if (!vectorStore.indexExists()) {
                log.error("Index 'my_vector_index' not found! Please create the index first.");
                return false;
            }


                float[] embeddings = embeddingModel.embed(descriptionBasic.getDescription());



               DescriptionBasic myIndex = DescriptionBasic.builder()
                       .id(UUID.randomUUID().toString())
                       .description(descriptionBasic.getDescription())
                       .store(descriptionBasic.getStore())
                       .embedding(embeddings)
                       .build();


                // method custom save
                IndexResponse response = elasticsearchClient.index(i -> i
                        .index("description_store_6c982379-4b94-4892-b948-57cf9997e9c1")
                        .id(myIndex.getId())
                        .document(myIndex)
                );


                  return true;

            }







    // create type elastic
    public boolean createIndex(String indexName) {


        try {
            // Kiểm tra nếu index đã tồn tại
            boolean exists = elasticsearchClient.indices().exists(e -> e.index(indexName)).value();
            if (exists) {
                System.out.println("Index existed: " + indexName);
                return false;
            }

            // Tạo index mới với settings và mappings
            CreateIndexResponse response = elasticsearchClient.indices().create(CreateIndexRequest.of(i -> i
                    .index(indexName)
                    .settings(s -> s
                            .numberOfShards("1")
                            .numberOfReplicas("1")
                    )
                    .mappings(m -> m
                            .properties("id", p -> p.keyword(k -> k))
                            .properties("content", p -> p.text(t -> t))
                            .properties("embedding", p -> p.denseVector(dv -> dv
                                    .dims(1024)
                                    .similarity("cosine")
                                    .index(true)
                            ))
                    )
            ));

            return response.acknowledged();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // create type elastic
    public boolean createIndexDescription(String indexName) {

        String index = "description_"+indexName;

        try {
            // Kiểm tra nếu index đã tồn tại
            boolean exists = elasticsearchClient.indices().exists(e -> e.index(index)).value();
            if (exists) {
                System.out.println("Index existed: " + indexName);
                return false;
            }

            // Tạo index mới với settings và mappings
            CreateIndexResponse response = elasticsearchClient.indices().create(CreateIndexRequest.of(i -> i
                    .index(index)
                    .settings(s -> s
                            .numberOfShards("1")
                            .numberOfReplicas("1")
                    )
                    .mappings(m -> m
                            .properties("id", p -> p.keyword(k -> k))
                            .properties("description", p -> p.text(t -> t))
                            .properties("store", p -> p.text(t -> t))
                            .properties("embedding", p -> p.denseVector(dv -> dv
                                    .dims(1024)
                                    .similarity("cosine")
                                    .index(true)
                            ))
                    )
            ));

            return response.acknowledged();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }



    private List<Float> convertToList(float[] vector) {
        List<Float> list = new ArrayList<>();
        for (float v : vector) {
            list.add(v); // Chuyển đổi float thành Float (autoboxing)
        }
        return list;
    }


    public SearchDBResponse searchDB(RequestDocument question) throws IOException {

       float[] questionEmbedding = embeddingModel.embed(question.getQuestion());

        SearchResponse<Document> response = null;


if(question.getStatus() == 1) {
      String indexName = "description_store_6c982379-4b94-4892-b948-57cf9997e9c1";


    SearchResponse<DocumentStore> responseStore = elasticsearchClient.search(s -> s
                    .index(indexName)
                    .query(q -> q
                            .scriptScore(ss -> ss
                                    .query(qq -> qq.matchAll(ma -> ma))
                                    .script(script -> script
                                            .source("cosineSimilarity(params.queryVector, 'embedding') + 1.0")
                                            .params("queryVector", JsonData.of(questionEmbedding)) // Truyền vector vào
                                    )
                            )
                    )
                    .size(1), // Số lượng tài liệu cần lấy
            DocumentStore.class
    );

    List<DocumentStore> resultStore = responseStore.hits().hits().stream()
            .map(Hit::source)
            .toList();


    response = elasticsearchClient.search(s -> s
                    .index(resultStore.get(0).getStore())
                    .query(q -> q
                            .scriptScore(ss -> ss
                                    .query(qq -> qq.matchAll(ma -> ma))
                                    .script(script -> script
                                            .source("cosineSimilarity(params.queryVector, 'embedding') + 1.0")
                                            .params("queryVector", JsonData.of(questionEmbedding)) // Truyền vector vào
                                    )
                            )
                    )
                    .size(5), // Số lượng tài liệu cần lấy
            Document.class
    );

}else{

    String indexName = "document-total-11-01-03";
    response = elasticsearchClient.search(s -> s
                    .index(indexName)
                    .query(q -> q
                            .scriptScore(ss -> ss
                                    .query(qq -> qq.matchAll(ma -> ma))
                                    .script(script -> script
                                            .source("cosineSimilarity(params.queryVector, 'embedding') + 1.0")
                                            .params("queryVector", JsonData.of(questionEmbedding)) // Truyền vector vào
                                    )
                            )
                    )
                    .size(5), // Số lượng tài liệu cần lấy
            Document.class
    );
}
        // Xử lý kết quả tìm kiếm
        List<Document> resultText = response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());

        return SearchDBResponse.builder()
                .resultText(resultText)
                .build();
    }


    public String searchDescriptionDB(RequestDocument question) throws IOException {

        float[] questionEmbedding = embeddingModel.embed(question.getQuestion());


        String indexName = "description_store_6c982379-4b94-4892-b948-57cf9997e9c1";


        SearchResponse<DocumentStore> response = elasticsearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .scriptScore(ss -> ss
                                        .query(qq -> qq.matchAll(ma -> ma))
                                        .script(script -> script
                                                .source("cosineSimilarity(params.queryVector, 'embedding') + 1.0")
                                                .params("queryVector", JsonData.of(questionEmbedding)) // Truyền vector vào
                                        )
                                )
                        )
                        .size(1), // Số lượng tài liệu cần lấy
                DocumentStore.class
        );



        List<DocumentStore> resultText = response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());

        if (!resultText.isEmpty()) {
            log.info(resultText.get(0).getStore()); // Lấy dữ liệu store
        }

        return resultText.get(0).getStore();
    }



}
