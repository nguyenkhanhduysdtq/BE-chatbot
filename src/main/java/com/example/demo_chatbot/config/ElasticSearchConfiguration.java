package com.example.demo_chatbot.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.model.ApiKey;
import org.springframework.ai.model.SimpleApiKey;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import javax.net.ssl.SSLContext;

@Configuration
@NonNullApi
@EnableElasticsearchRepositories(basePackages = "com.example.demo_chatbot.repository")
@Slf4j
public class ElasticSearchConfiguration extends ElasticsearchConfiguration  {


    @Value("${spring.ai.openai.api-key}")
    String OPENAI_API_KEY ;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedToLocalhost()
                .usingSsl(buiSslContext())
                .withBasicAuth("elastic","*3xI7rUZlHRi6hCJ+JWB")
                .build();
    }



    private static SSLContext buiSslContext(){
        try{
            return new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();
        }catch (Exception e){

            throw new RuntimeException(e);

        }
    }





    @Bean
    public EmbeddingModel embeddingModel() {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        OpenAiApi openAiApi = new OpenAiApi(
                "https://api.openai.com/v1",  // Base URL của OpenAI
                (ApiKey) new SimpleApiKey(OPENAI_API_KEY),
                headers,
                "/completions",
                "/embeddings",
                RestClient.builder(),
                WebClient.builder(),
                new DefaultResponseErrorHandler()
        );
        return new OpenAiEmbeddingModel(openAiApi
                ,MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder()
                        .model("text-embedding-3-large")
                        .dimensions(1024)
                        .build(),
                RetryUtils.DEFAULT_RETRY_TEMPLATE
        );

    }


    @Bean
    public ElasticsearchClient elasticsearchClient() {
        org.elasticsearch.client.RestClient restClient = org.elasticsearch.client.RestClient.builder(
                new HttpHost("localhost", 9200, "http")
        ).build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper()
        );

        return new ElasticsearchClient(transport);
    }



}
