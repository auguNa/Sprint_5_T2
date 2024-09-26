package S5_T2.IT_ACADEMY.service;

import java.util.Set;

public interface AuthService {

    void registerUser(String username, String password, Set<String> roleNames);

    void registerAdmin(String username, String password, Set<String> roleNames);

    String loginUser(String username, String password);

}
