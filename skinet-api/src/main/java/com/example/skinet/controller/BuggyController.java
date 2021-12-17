package com.example.skinet.controller;

import com.example.skinet.error.ApiException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/buggy")
public class BuggyController {

    @GetMapping("not-found")
    ResponseEntity<String> notFound() {
        throw new ApiException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("maths/{id}")
    ResponseEntity<Integer> maths(@PathVariable Integer id) {
        return ResponseEntity.ok(10 / id);
    }

    @GetMapping("server-error")
    ResponseEntity<String> serverError() {
        throw new NullPointerException();
    }

    @GetMapping("bad-request")
    ResponseEntity<String> badRequest() {
        throw new ApiException(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("save")
    ResponseEntity<TestForm> saveModel(@Valid @RequestBody TestForm form) {
        return ResponseEntity.ok(form);
    }

    @GetMapping("bad-request/{id}")
    ResponseEntity<Integer> badRequestWithId(@PathVariable int id) {
        return ResponseEntity.ok(id);
    }

    @Data
    private static class TestForm {
        @NotBlank
        private String name;
    }
}
