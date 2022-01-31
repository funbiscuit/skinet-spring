package com.example.skinet.controller;

import com.example.skinet.core.entity.Address;
import com.example.skinet.core.entity.LoginDTO;
import com.example.skinet.core.entity.RegisterDTO;
import com.example.skinet.core.entity.UserDTO;
import com.example.skinet.error.ApiException;
import com.example.skinet.service.AuthService;
import com.example.skinet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDTO> currentUser(Principal principal) {
        return ResponseEntity.ok(authService.getUserByEmail(principal.getName()));
    }

    @GetMapping("email-exists")
    public ResponseEntity<Boolean> isEmailUsed(String email) {
        return ResponseEntity.ok(userService.getUser(email).isPresent());
    }

    @GetMapping("address")
    public ResponseEntity<Address> getAddress(Principal principal) {
        Address address = userService.getUser(principal.getName()).orElseThrow().getAddress();
        if (address == null) {
            throw ApiException.addressNotFound(principal.getName());
        }
        return ResponseEntity.ok(address);
    }

    @PutMapping("address")
    public ResponseEntity<Address> setAddress(Principal principal, @RequestBody Address address) {

        return ResponseEntity.ok(userService.setUserAddress(principal.getName(), address));
    }

    @PostMapping("login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginDTO loginDto) {
        return ResponseEntity.ok(authService.loginUser(loginDto));
    }

    @PostMapping("register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(authService.registerUser(registerDTO));
    }

}
