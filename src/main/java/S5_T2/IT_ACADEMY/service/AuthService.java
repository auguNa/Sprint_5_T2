package S5_T2.IT_ACADEMY.service;

import S5_T2.IT_ACADEMY.entity.Role;
import S5_T2.IT_ACADEMY.entity.UserEntity;
import org.springframework.cache.annotation.Cacheable;

import java.util.Set;

public interface AuthService {

    void registerUser(String username, String password, Set<String> roleNames);

    void registerAdmin(String username, String password, Set<String> roleNames);

    String loginUser(String username, String password);


    Role findRoleByName(String roleName);

    @Cacheable(value = "users", key = "#username")
    UserEntity getUserByUsername(String username);
}
