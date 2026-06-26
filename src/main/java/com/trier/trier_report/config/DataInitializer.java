package com.trier.trier_report.config;

import com.trier.trier_report.dao.AccountTypeRepository;
import com.trier.trier_report.dao.CurrencyRepository;
import com.trier.trier_report.dao.RoleRepository;
import com.trier.trier_report.dao.UserRepository;
import com.trier.trier_report.entity.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder encoder, AccountTypeRepository accountTypeRepository, CurrencyRepository currencyRepository) {
        return args -> {
            Role role = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role not found"));
            Optional<User> u = userRepository.findByEmail("test@email.com");

            if (u.isEmpty()) {

                User user = new User("testFirstname", "testLastname", "test@email.com", encoder.encode("testPassword1234"), role.getId());

                User savedUser = userRepository.save(user);
                System.out.println("User initialized.");

                AccountType accountType = accountTypeRepository.findByName("BANK").orElseThrow(() -> new RuntimeException("Account type not found"));
                Currency currency = currencyRepository.findByName("EUR").orElseThrow(() -> new RuntimeException("Currency not found"));
                Account account = new Account(savedUser.getId(), accountType.getId(), currency.getId(), "First account");
            }
        };
    }
}
