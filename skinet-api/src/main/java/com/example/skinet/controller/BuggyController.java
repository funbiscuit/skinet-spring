package com.example.skinet.controller;

import com.example.skinet.error.ApiException;
import com.example.skinet.error.ExtendedErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping(value = "/buggy", produces = {MediaType.APPLICATION_JSON_VALUE})
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

    @ApiResponse(responseCode = "200", description = "Form is saved",
            content = {@Content(schema = @Schema(implementation = TestForm.class))})
    @ApiResponse(responseCode = "400", description = "Invalid form supplied",
            content = {@Content(schema = @Schema(implementation = ExtendedErrorResponse.class))})
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
