package com.example.FileReader.bean;

import com.example.FileReader.entiry.LocalFiles;
import com.example.FileReader.repo.LocalFilesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
public class MultiThreadAddLocalFile extends Thread{
    private LocalFilesRepo localFilesRepo;

    public MultiThreadAddLocalFile(LocalFilesRepo localFilesRepo) {
        this.localFilesRepo = localFilesRepo;
    }

    @Override
    public void run(){
        String folderPath = "C:/Users/Admin/Documents/user_documents/файлы/user_documents/";

        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        LocalFiles localFiles = localFilesRepo.findByFileName(file.getName());
                        if (localFiles!=null)
                            continue;
                        localFiles = new LocalFiles();
                        localFiles.setFileName(file.getName());
                        localFiles.setMemory(file.length());
                        localFilesRepo.save(localFiles);
                    }
                }
            } else {
                System.out.println("Папка пуста");
            }
        } else {
            System.out.println("Указанный путь не является директорией или не существует");
        }
    }
}
