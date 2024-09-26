package S5_T2.IT_ACADEMY.service.implementation;

import S5_T2.IT_ACADEMY.entity.Role;
import S5_T2.IT_ACADEMY.entity.UserEntity;
import S5_T2.IT_ACADEMY.exception.UserAlreadyExistsException;
import S5_T2.IT_ACADEMY.repository.RoleRepository;
import S5_T2.IT_ACADEMY.repository.UserRepository;
import S5_T2.IT_ACADEMY.service.AuthService;
import S5_T2.IT_ACADEMY.service.CustomUserDetailsService;
import S5_T2.IT_ACADEMY.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    public void registerUser(String username, String password, Set<String> roleNames) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("Error: Username '" + username + "' already exists.");
        }
        // Fetch roles from the database
        Set<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Error: Role " + roleName + " not found.")))
                .collect(Collectors.toSet());
        // Create a new user and assign the fetched roles
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setRoles(roles);
        userRepository.save(userEntity);
    }

    public void registerAdmin(String username, String password, Set<String> roleNames) {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setRoles(Collections.singleton(adminRole));

        userRepository.save(userEntity);
    }

    public String loginUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Fetch user roles and add them to the JWT token
        List<String> roles = userEntity.getRoles().stream()
                .map(role -> role.getName())  // Assuming Role has a getName() method
                .collect(Collectors.toList());

        return jwtUtil.generateToken(userDetails, roles);
    }
    public Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Error: Role " + roleName + " not found."));
    }
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
