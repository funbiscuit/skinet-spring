package com.example.skinet.controller;

import com.example.skinet.error.ApiException;
import com.example.skinet.error.ExtendedErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.Principal;

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

    @GetMapping("test-auth")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    ResponseEntity<String> testAuth(Principal principal) {
        return ResponseEntity.ok("Secret for " + principal.getName());
    }

    @GetMapping("test-auth-admin")
    @Secured("ROLE_ADMIN")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    ResponseEntity<String> testAuthAdmin(Principal principal) {
        return ResponseEntity.ok("Super secret for " + principal.getName());
    }

    @GetMapping("test-auth2-admin")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    ResponseEntity<String> testAuth2Admin(Principal principal) {
        return ResponseEntity.ok("Super secret2 for " + principal.getName());
    }

    @Data
    private static class TestForm {
        @NotBlank
        private String name;
    }
}
