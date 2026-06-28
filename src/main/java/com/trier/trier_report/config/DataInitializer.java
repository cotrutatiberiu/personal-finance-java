package com.trier.trier_report.config;

import com.trier.trier_report.dao.*;
import com.trier.trier_report.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@DependsOn("dataSourceScriptDatabaseInitializer")
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AccountTypeRepository accountTypeRepository;
    private final CurrencyRepository currencyRepository;
    private final AccountRepository accountRepository;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository,
                           PasswordEncoder encoder, AccountTypeRepository accountTypeRepository,
                           CurrencyRepository currencyRepository, AccountRepository accountRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.accountTypeRepository = accountTypeRepository;
        this.currencyRepository = currencyRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Role role = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role not found"));
        Currency currency = currencyRepository.findByNameIgnoreCase("EUR").orElseThrow(() -> new RuntimeException("Currency not found"));
        Optional<User> u = userRepository.findByEmail("test@email.com");

        if (u.isEmpty()) {
            User user = new User("testFirstname", "testLastname", "test@email.com",
                    encoder.encode("testPassword1234"), role.getId());

            User savedUser = userRepository.save(user);
            System.out.println("User initialized.");

            AccountType accountType = accountTypeRepository.findByName("BANK")
                    .orElseThrow(() -> new RuntimeException("Account type not found"));

            Account account = new Account(savedUser.getId(), accountType.getId(), currency.getId(), "First account");
            accountRepository.save(account);
            System.out.println("Account initialized.");
        }
    }
}
