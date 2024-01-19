package com.example.FileReader.repo;

import com.example.FileReader.entiry.LocalFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalFilesRepo extends JpaRepository<LocalFiles, Integer> {

    LocalFiles findByFileName(String name);

    @Query("SELECT fileName FROM LocalFiles")
    List<String> findAllFileNames();
}
