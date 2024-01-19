package com.example.FileReader.controller;


import com.example.FileReader.bean.Result;
import com.example.FileReader.service.FileService;
import com.example.FileReader.utils.ParamUtil;
import com.example.FileReader.utils.PojUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private FileService fileService;

    @PostMapping("/start")
    @ResponseBody
    public Result start(@RequestBody HashMap<String, Object> params, HttpServletRequest request) {
        try {
            return fileService.start(params, request);
        } catch (ServiceException ex) {
            log.error(ParamUtil.request_info_has_log(""), ex);
            return new Result(-3, ex.getMessage(), null);
        } catch (Exception e) {
            log.error(ParamUtil.request_info_has_log(""), e);
            return new Result(-3, e.getMessage(), PojUtil.getStackTrace(e));
        }
    }
    @PostMapping("/local")
    @ResponseBody
    public Result local(@RequestBody HashMap<String, Object> params, HttpServletRequest request) {
        try {
            return fileService.local(params, request);
        } catch (ServiceException ex) {
            log.error(ParamUtil.request_info_has_log(""), ex);
            return new Result(-3, ex.getMessage(), null);
        } catch (Exception e) {
            log.error(ParamUtil.request_info_has_log(""), e);
            return new Result(-3, e.getMessage(), PojUtil.getStackTrace(e));
        }
    }
    @PostMapping("/ps")
    @ResponseBody
    public Result ps(@RequestBody HashMap<String, Object> params, HttpServletRequest request) {
        try {
            return fileService.ps(params, request);
        } catch (ServiceException ex) {
            log.error(ParamUtil.request_info_has_log(""), ex);
            return new Result(-3, ex.getMessage(), null);
        } catch (Exception e) {
            log.error(ParamUtil.request_info_has_log(""), e);
            return new Result(-3, e.getMessage(), PojUtil.getStackTrace(e));
        }
    }

}
