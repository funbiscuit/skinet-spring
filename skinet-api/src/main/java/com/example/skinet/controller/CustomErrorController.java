package com.example.skinet.controller;

import com.example.skinet.error.ApiException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomErrorController implements ErrorController {
    @RequestMapping
    public String handleError(HttpServletRequest request) {
        throw new ApiException(HttpStatus.resolve(
                (Integer) request.getAttribute("javax.servlet.error.status_code")));
    }
}
