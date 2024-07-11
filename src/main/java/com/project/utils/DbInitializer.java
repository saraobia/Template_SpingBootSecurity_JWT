package com.project.utils;

import com.project.model.Role;
import com.project.model.User;
import com.project.model.enums.RoleCode;
import com.project.repository.RoleRepository;
import com.project.repository.UserRepository;
import org.apache.catalina.startup.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (!roleRepository.existsByRoleCode(RoleCode.ROLE_USER)) {
            roleRepository.save(Role.builder().role(RoleCode.ROLE_USER).build());
        }
        if (!roleRepository.existsByRoleCode(RoleCode.ROLE_ADMIN)) {
            roleRepository.save(Role.builder().role(RoleCode.ROLE_ADMIN).build());
        }

        Role roleAdmin = roleRepository.findByRoleCode(RoleCode.ROLE_ADMIN).orElseThrow();
        Role roleUser = roleRepository.findByRoleCode(RoleCode.ROLE_USER).orElseThrow();

        List<User> users = List.of(
                User.builder()
                        .email("pippo@mail.com")
                        .password(passwordEncoder.encode("1234"))
                        .name("Pippo")
                        .surname("Rossi")
                        .roles(List.of(roleAdmin))
                        .build(),
                User.builder()
                        .email("pluto@mail.com")
                        .password(passwordEncoder.encode("5678"))
                        .name("Pluto")
                        .surname("Bianchi")
                        .roles(List.of(roleUser))
                        .build(),
                User.builder()
                        .email("paperino@mail.com")
                        .password(passwordEncoder.encode("abcd"))
                        .name("Paperino")
                        .surname("Verdi")
                        .roles(List.of(roleUser))
                        .build(),
                User.builder()
                        .email("minnie@mail.com")
                        .password(passwordEncoder.encode("efgh"))
                        .name("Minnie")
                        .surname("Neri")
                        .roles(List.of(roleUser))
                        .build(),
                User.builder()
                        .email("topolino@mail.com")
                        .password(passwordEncoder.encode("ijkl"))
                        .name("Topolino")
                        .surname("Gialli")
                        .roles(List.of(roleAdmin))
                        .build()
        );

        for (User user : users) {
            if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
                userRepository.save(user);
            }
        }
    }
}
