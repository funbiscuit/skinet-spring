package com.example.skinet.service;

import com.example.skinet.core.entity.Address;
import com.example.skinet.core.entity.AppUser;
import com.example.skinet.core.entity.Role;
import com.example.skinet.error.ApiException;
import com.example.skinet.repo.AddressRepository;
import com.example.skinet.repo.RoleRepository;
import com.example.skinet.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser createUser(AppUser user, String plainPassword) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(u -> {
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Email already used");
                });
        user.setPassHash(passwordEncoder.encode(plainPassword));
        user = userRepository.save(user);
        if (user.getAddress() != null && user.getAddress().getId() == null) {
            user.getAddress().setAppUser(user);
            user.setAddress(addressRepository.save(user.getAddress()));
        }
        return user;
    }

    @Override
    public Address setUserAddress(String email, Address address) {
        AppUser user = userRepository.findByEmail(email).orElseThrow();

        if (user.getAddress() == null) {
            address.setAppUser(user);
            user.setAddress(addressRepository.save(address));
        } else {
            user.getAddress().setFieldsFrom(address);
        }

        return user.getAddress();
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.findByName(role.getName())
                .orElseGet(() -> roleRepository.save(role));
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        Role role = roleRepository.findByName(roleName).orElseThrow();
        AppUser user = userRepository.findByEmail(email).orElseThrow();

        user.getRoles().add(role);
    }

    @Override
    public Optional<AppUser> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public long getUsersCount() {
        return userRepository.count();
    }

    /**
     * email is used instead of username
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        AppUser user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with email " + username + " not found"));
        return new User(user.getEmail(),
                user.getPassHash(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
    }
}
