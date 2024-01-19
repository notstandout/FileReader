package com.example.FileReader.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.example.FileReader.bean.S3Service.s3Client;


public class BucketService {

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);
    public ListObjectsResponse response;

    private void getBucketFiles(){
        ListBucketsResponse listBucketsResponse = s3Client.listBuckets();;

        logger.info("Список бакетов:");

        for (Bucket bucket : listBucketsResponse.buckets()) {
            if (bucket.name().equals("ship")) {

                response = s3Client.listObjects(ListObjectsRequest.builder().bucket(bucket.name()).build());

                for (S3Object s3 : response.contents()) {
                    String objectKey = s3.key();
                    if (!objectKey.isEmpty()) {
                        System.out.println(objectKey + " downloaded successfully.");
                    }
                }
            }
        }

    }
    private void downloadFile(String bucketName, String objectKey) {
        try {
            String sanitizedFileName = sanitizeFileName(objectKey);

            // Extract subdirectories if they exist
            String[] directories = objectKey.split("/");

            // Build the full directory path
            StringBuilder directoryPath = new StringBuilder("C:/Users/Admin/Desktop/S3Files/");
            for (int i = 0; i < directories.length - 1; i++) {
                directoryPath.append(sanitizeFileName(directories[i])).append(File.separator);
            }

            // Append the sanitized file name to the directory path
            directoryPath.append(sanitizeFileName(directories[directories.length - 1]));

            File localFile = new File(directoryPath.toString());
            if (!localFile.exists()) {
                // File doesn't exist, proceed with download

                // Create directories if they don't exist
                File directory = new File(directoryPath.toString());
                try {
                    if (!directory.exists()) {
                        System.out.println("Creating directory: " + directoryPath);
                        Files.createDirectories(directory.toPath());
                    }
                } catch (IOException e) {
                    System.err.println("Error creating directory: " + e.getMessage());
                    e.printStackTrace();
                }

                // Print debug information
                System.out.println("Directory: " + directoryPath);
                System.out.println("File Name: " + sanitizedFileName);
                System.out.println("Local File Path: " + localFile.getAbsolutePath());

                // Download the object from S3 and save it locally
                s3Client.getObject(GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build(), localFile.toPath());

                System.out.println("File downloaded successfully: " + localFile.getAbsolutePath());
            } else {
                // File already exists, skip download
                System.out.println("File already exists, skipping download: " + localFile.getAbsolutePath());
            }
        } catch (S3Exception e) {
            System.err.println("Error downloading file from S3. S3Exception details: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Sanitization logic as mentioned in the previous response
    private String sanitizeFileName(String originalFileName) {

        String sanitizedFileName = originalFileName.replaceAll(" ", "_");


        sanitizedFileName = sanitizedFileName.replaceAll("[^\\x00-\\x7F]", "");


        sanitizedFileName = sanitizedFileName.replaceAll("[^a-zA-Z0-9.-]", "");

        return sanitizedFileName;
    }

}
