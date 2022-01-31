package com.example.skinet.service;

import com.example.skinet.core.entity.Address;
import com.example.skinet.core.entity.AppUser;
import com.example.skinet.core.entity.Role;

import java.util.Optional;

public interface UserService {
    AppUser createUser(AppUser user, String plainPassword);

    Address setUserAddress(String email, Address address);

    Role createRole(Role role);

    void addRoleToUser(String email, String roleName);

    Optional<AppUser> getUser(String email);

    long getUsersCount();
}
