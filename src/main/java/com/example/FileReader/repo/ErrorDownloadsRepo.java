package com.example.FileReader.repo;

import com.example.FileReader.entiry.ErrorDownloads;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorDownloadsRepo extends JpaRepository<ErrorDownloads, Integer> {

    ErrorDownloads findByFileName(String name);
}
