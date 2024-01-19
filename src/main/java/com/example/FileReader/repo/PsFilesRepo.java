package com.example.FileReader.repo;

import com.example.FileReader.entiry.PsFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PsFilesRepo extends JpaRepository<PsFiles, Integer> {

    PsFiles findByFileName(String name);

    @Query("SELECT fileName FROM PsFiles")
    List<String> findAllFileNames();

}
