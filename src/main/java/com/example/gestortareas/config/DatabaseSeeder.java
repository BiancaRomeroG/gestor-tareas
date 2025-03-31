package com.example.gestortareas.config;

import com.example.gestortareas.data.models.Role;
import com.example.gestortareas.data.models.User;
import com.example.gestortareas.respositories.RoleRepository;
import com.example.gestortareas.respositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Bean
    public CommandLineRunner initDatabase(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (roleRepository.count() == 0) {
                Role adminRole = Role.builder().name("ADMIN").build();
                Role userRole = Role.builder().name("USER").build();
                roleRepository.saveAll(List.of(adminRole, userRole));
                System.out.println("Roles iniciales insertados.");
            }

            if (userRepository.count() == 0) {
                User adminUser = User.builder()
                        .name("Admin")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("admin123"))
                        .build();
                userRepository.save(adminUser);
                System.out.println("Usuario admin inicial insertado.");
            }
        };
    }
}
