package com.project.utils;

import com.project.model.Role;
import com.project.model.User;
import com.project.model.enums.RoleCode;
import com.project.repository.RoleRepository;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public void run(String... args) throws Exception {

        initializeRolesAndUsers();

    }

    private void initializeRolesAndUsers() {
        if (!roleRepository.existsByRoleCode(RoleCode.ROLE_USER)) {
            roleRepository.save(Role.builder().role(RoleCode.ROLE_USER).build());
        }
        if (!roleRepository.existsByRoleCode(RoleCode.ROLE_ADMIN)) {
            roleRepository.save(Role.builder().role(RoleCode.ROLE_ADMIN).build());
        }
        Role roleAdmin = roleRepository.findByRoleCode(RoleCode.ROLE_ADMIN).orElseThrow();
        Role roleUser = roleRepository.findByRoleCode(RoleCode.ROLE_USER).orElseThrow();

        if(userRepository.findByEmail("admin@mail.com").isEmpty()) {
            userRepository.save(User.builder()
                    .email("@")
                    .password(passwordEncoder.encode("12"))
                    .name("Admin")
                    .surname("User")
                    .roles(List.of(roleAdmin))
                    .build());
        }

        if(userRepository.findByEmail("user@mail.com").isEmpty()) {
            userRepository.save(User.builder()
                    .email("user@mail.com")
                    .password(passwordEncoder.encode("user1234"))
                    .name("Regular")
                    .surname("User")
                    .roles(List.of(roleUser))
                    .build());
        }

        if(userRepository.findByEmail("empty@mail.com").isEmpty()) {
            userRepository.save(User.builder()
                    .email("empty@mail.com")
                    .password(passwordEncoder.encode("empty1234"))
                    .name("Empty")
                    .surname("User")
                    .roles(List.of(roleUser))
                    .build());
        }
    }

}
