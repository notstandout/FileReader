package com.example.FileReader.service;

import com.example.FileReader.bean.MultiThreadAddLocalFile;
import com.example.FileReader.bean.MultiThreadAddPsFile;
import com.example.FileReader.bean.Result;
import com.example.FileReader.entiry.ErrorDownloads;
import com.example.FileReader.repo.ErrorDownloadsRepo;
import com.example.FileReader.repo.LocalFilesRepo;
import com.example.FileReader.repo.PsFilesRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class FileServiceImpl implements FileService{

    @Autowired
    LocalFilesRepo localFilesRepo;
    @Autowired
    PsFilesRepo psFilesRepo;
    @Autowired
    ErrorDownloadsRepo errorDownloadsRepo;

    @Override
    public Result start(HashMap<String, Object> params, HttpServletRequest request) {
        List<String> localNames = localFilesRepo.findAllFileNames();
        List<String> psNames = psFilesRepo.findAllFileNames();

//        psNames.removeAll(localNames);
//
//        for (String s: psNames){
//            ErrorDownloads errorDownloads = errorDownloadsRepo.findByFileName(s);
//            if (errorDownloads!=null)
//                continue;
//
//            errorDownloads = new ErrorDownloads();
//            errorDownloads.setFileName(s);
//            errorDownloads.setMemory(0L);
//            errorDownloadsRepo.save(errorDownloads);
//        }

        localNames.removeAll(psNames);

        for (String s: localNames){
            ErrorDownloads errorDownloads = errorDownloadsRepo.findByFileName(s);
            if (errorDownloads!=null)
                continue;

            errorDownloads = new ErrorDownloads();
            errorDownloads.setFileName(s);
            errorDownloads.setMemory(0L);
            errorDownloadsRepo.save(errorDownloads);
        }


        return new Result(0, "OK", "success");
    }

    @Override
    public Result local(HashMap<String, Object> params, HttpServletRequest request) {
        MultiThreadAddLocalFile multiThreadAddLocalFile = new MultiThreadAddLocalFile(localFilesRepo);
        multiThreadAddLocalFile.start();
        return new Result(0, "OK", multiThreadAddLocalFile);
    }

    @Override
    public Result ps(HashMap<String, Object> params, HttpServletRequest request) {
        MultiThreadAddPsFile multiThreadAddPsFile = new MultiThreadAddPsFile(psFilesRepo);
        multiThreadAddPsFile.start();
        return new Result(0, "OK", multiThreadAddPsFile);
    }
}
