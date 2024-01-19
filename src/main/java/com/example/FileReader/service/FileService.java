package com.example.FileReader.service;

import com.example.FileReader.bean.Result;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;

public interface FileService {
    Result start(HashMap<String, Object> params, HttpServletRequest request);

    Result local(HashMap<String, Object> params, HttpServletRequest request);

    Result ps(HashMap<String, Object> params, HttpServletRequest request);
}
