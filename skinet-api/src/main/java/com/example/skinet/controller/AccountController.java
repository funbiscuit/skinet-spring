package com.example.skinet.controller;

import com.example.skinet.core.entity.*;
import com.example.skinet.error.ApiException;
import com.example.skinet.service.AuthService;
import com.example.skinet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AuthService authService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<UserDTO> currentUser(Principal principal) {
        return ResponseEntity.ok(authService.getUserByEmail(principal.getName()));
    }

    @GetMapping("email-exists")
    public ResponseEntity<Boolean> isEmailUsed(String email) {
        return ResponseEntity.ok(userService.getUser(email).isPresent());
    }

    @GetMapping("address")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<AddressDTO> getAddress(Principal principal) {
        Address address = userService.getUser(principal.getName()).orElseThrow().getAddress();
        if (address == null) {
            throw ApiException.addressNotFound(principal.getName());
        }
        return ResponseEntity.ok(modelMapper.map(address, AddressDTO.class));
    }

    @PutMapping("address")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<AddressDTO> setAddress(Principal principal,
                                                 @RequestBody @Valid AddressDTO address) {
        Address newAddress = userService.setUserAddress(principal.getName(),
                modelMapper.map(address, Address.class));
        return ResponseEntity.ok(modelMapper.map(newAddress, AddressDTO.class));
    }

    @PostMapping("login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginDTO loginDto) {
        return ResponseEntity.ok(authService.loginUser(loginDto));
    }

    @PostMapping("register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid RegisterDTO registerDTO) {
        return ResponseEntity.ok(authService.registerUser(registerDTO));
    }

}
