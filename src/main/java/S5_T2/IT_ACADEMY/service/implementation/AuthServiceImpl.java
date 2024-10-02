package S5_T2.IT_ACADEMY.service.implementation;

import S5_T2.IT_ACADEMY.entity.Role;
import S5_T2.IT_ACADEMY.entity.UserEntity;
import S5_T2.IT_ACADEMY.exception.UserAlreadyExistsException;
import S5_T2.IT_ACADEMY.repository.RoleRepository;
import S5_T2.IT_ACADEMY.repository.UserRepository;
import S5_T2.IT_ACADEMY.service.AuthService;
import S5_T2.IT_ACADEMY.service.CustomUserDetailsService;
import S5_T2.IT_ACADEMY.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.github.benmanes.caffeine.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
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
    @Autowired
    private Cache<String, String> jwtTokenCache;

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

    @Override
    @CachePut(value = "jwtTokens", key = "#username")
    public String loginUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (jwtTokenCache.getIfPresent(username) != null) {
            logger.info("JWT token for user '{}' found in cache", username);
        } else {
            logger.info("JWT token for user '{}' not found in cache, generating new token", username);
        }
        // Fetch user roles and add them to the JWT token
        List<String> roles = userEntity.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());

        String token = jwtUtil.generateToken(userDetails, roles);
        jwtTokenCache.put(username, token); // Store the generated token in the cache

        return token;
    }

    @Override
    public Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Error: Role " + roleName + " not found."));
    }

    @Cacheable(value = "users", key = "#username")
    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
