package com.example.FileReader.bean;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

@Service
public class S3Service {
    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    @Value("${access_key}")
    private String accessKey;

    @Value("${secret_key}")
    private String secretKey;

    @Value("${s3.endpoint}")
    private String s3Endpoint;

    public static S3Client s3Client;

    @PostConstruct
    private void connect() {
        try {
            AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);

            s3Client = S3Client.builder()
                    .region(Region.CA_CENTRAL_1)
                    .endpointOverride(URI.create(s3Endpoint))
                    .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                    .build();

            logger.info("Подключение к S3 выполнено успешно.");
        } catch (Exception e) {
            System.err.println("Ошибка при подключении к S3: " + e.getMessage());
            logger.error("Произошла ошибка при подключении к S3.", e);
        }
    }
}