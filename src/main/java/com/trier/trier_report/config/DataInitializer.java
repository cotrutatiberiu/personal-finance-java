package com.trier.trier_report.config;

import com.trier.trier_report.dao.*;
import com.trier.trier_report.entity.*;
import com.trier.trier_report.service.UserService;
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
    private final UserService userService;
    private final AccountTypeRepository accountTypeRepository;
    private final CurrencyRepository currencyRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final UserRoleRepository userRoleRepository;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, UserService userService,
                           PasswordEncoder encoder, AccountTypeRepository accountTypeRepository,
                           CurrencyRepository currencyRepository, AccountRepository accountRepository, CategoryRepository categoryRepository, UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.accountTypeRepository = accountTypeRepository;
        this.currencyRepository = currencyRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Role moderatorRole = roleRepository.findByName(RoleType.MODERATOR).orElseThrow(() -> new RuntimeException("Role not found"));
        Currency currency = currencyRepository.findByNameIgnoreCase("EUR").orElseThrow(() -> new RuntimeException("Currency not found"));

        String firstName = "moderatorFirstname";
        String lastName = "moderatorLastname";
        String email = "testModerator@email.com";
        String password = "moderatorPassword1234";
        String parentCategoryName = "food";
        String subCategoryName = "restaurants";
        String accountName = "BANK";

        Optional<User> u = userRepository.findByEmail(email);

        if (u.isEmpty()) {

            User user = new User(firstName, lastName, email);
            User savedUser = userService.create(user, password);
            System.out.println("User initialized.");

            AccountType accountType = accountTypeRepository.findByName(accountName)
                    .orElseThrow(() -> new RuntimeException("Account type not found"));

            Account account = new Account(savedUser.getId(), accountType.getId(), currency.getId(), "First account");
            accountRepository.save(account);
            System.out.println("Account initialized.");

            Category parentCategory = new Category(savedUser.getId(), null, parentCategoryName);
            categoryRepository.save(parentCategory);

            Category savedParentCategory = categoryRepository.findByUserId(savedUser.getId()).orElseThrow(() -> new RuntimeException("Category not found"));

            Category subCategory = new Category(savedUser.getId(), savedParentCategory.getId(), subCategoryName);
            categoryRepository.save(subCategory);

            UserRole userRole = new UserRole(savedUser, moderatorRole);
            userRoleRepository.save(userRole);
        }
    }
}
