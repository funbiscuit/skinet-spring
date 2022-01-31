package com.example.skinet.service;

import com.example.skinet.core.entity.Address;
import com.example.skinet.core.entity.AppUser;
import com.example.skinet.core.entity.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersInitializer {

    private final UserService userService;

    @Transactional
    public void seedUsers() {
        if (userService.getUsersCount() > 0) {
            return;
        }

        log.info("No users exist. Seeding users data.");

        userService.createRole(new Role("ROLE_USER"));
        userService.createRole(new Role("ROLE_ADMIN"));

        AppUser bob = new AppUser("bob@gmail.com", "Bob",
                new Address("Bob", "Bobbity", "10 The Street",
                        "New York", "NY", "90210", "USA"));

        userService.createUser(bob, "123");

        userService.addRoleToUser("bob@gmail.com", "ROLE_USER");
        userService.addRoleToUser("bob@gmail.com", "ROLE_ADMIN");
    }
}
