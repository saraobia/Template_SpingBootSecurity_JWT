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
        if(userRepository.findByEmail("user@mail").isEmpty()) {
            userRepository.save(User.builder()
                    .email("user@mail")
                    .password(passwordEncoder.encode("prova1234"))
                    .name("User")
                    .surname("Surname")
                    .roles(List.of(roleAdmin))
                    .build());
        }
    }
}
