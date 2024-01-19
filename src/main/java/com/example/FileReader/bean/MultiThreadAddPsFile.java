package com.example.FileReader.bean;

import com.example.FileReader.entiry.PsFiles;
import com.example.FileReader.repo.PsFilesRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.FileReader.bean.S3Service.s3Client;

public class MultiThreadAddPsFile extends Thread {

    PsFilesRepo psFilesRepo;

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);
    private static final S3Client s3Client = S3Service.s3Client;

    public MultiThreadAddPsFile(PsFilesRepo psFilesRepo){
        this.psFilesRepo = psFilesRepo;
    }


    @Override
    public void run() {
        String bucketName = "user-documets";
        String targetPrefix = "user_documents/";

        List<String> uniqueSubfolders = listSubfolders(bucketName, targetPrefix);

        uniqueSubfolders.forEach(subfolder -> {
            try {
                    PsFiles psFiles = psFilesRepo.findByFileName(subfolder);
                    if (psFiles==null){
                        psFiles = new PsFiles();
                        psFiles.setFileName(subfolder);
                        psFiles.setMemory(0L);
                        psFilesRepo.save(psFiles);
                    }
            } catch (Exception e) {
                logger.error("Error processing subfolder: " + subfolder, e);
            }
        });

        System.out.println("FINISHED");
    }

    private static List<String> listSubfolders(String bucketName, String prefix) {
        List<String> uniqueSubfolders = new ArrayList<>();

        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .delimiter("/")
                .build();

        ListObjectsV2Response response;
        do {
            response = s3Client.listObjectsV2(listObjectsV2Request);

            for (CommonPrefix commonPrefix : response.commonPrefixes()) {
                String subfolderName = commonPrefix.prefix().substring(prefix.length());
                subfolderName = subfolderName.endsWith("/") ? subfolderName.substring(0, subfolderName.length() - 1) : subfolderName;

                uniqueSubfolders.add(subfolderName);
            }

            listObjectsV2Request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .delimiter("/")
                    .continuationToken(response.nextContinuationToken())
                    .build();

        } while (response.isTruncated());

        return uniqueSubfolders;
    }
}
