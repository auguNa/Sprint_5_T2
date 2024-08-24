package S5_T2.IT_ACADEMY.init;

import S5_T2.IT_ACADEMY.entity.Role;
import S5_T2.IT_ACADEMY.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public CommandLineRunner initializeRoles() {
        return args -> {
            if (!roleRepository.existsByName("ROLE_USER")) {
                Role roleUser = new Role();
                roleUser.setName("ROLE_USER");
                roleRepository.save(roleUser);
            }

            if (!roleRepository.existsByName("ROLE_ADMIN")) {
                Role roleAdmin = new Role();
                roleAdmin.setName("ROLE_ADMIN");
                roleRepository.save(roleAdmin);
            }
        };
    }
}