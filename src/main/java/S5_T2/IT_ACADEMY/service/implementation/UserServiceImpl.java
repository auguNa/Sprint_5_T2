package S5_T2.IT_ACADEMY.service.implementation;

import S5_T2.IT_ACADEMY.entity.UserEntity;
import S5_T2.IT_ACADEMY.repository.UserRepository;
import S5_T2.IT_ACADEMY.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Cacheable(value = "users", key = "#username")
    public UserEntity getUserSettings(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserEntity> getAllUsers(String userDetails) {
        return userRepository.findAll();
    }

    @Override
    public void createUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public void updateUser(Long userId, UserEntity updatedUserEntity) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        // Update user details
        userEntity.setUsername(updatedUserEntity.getUsername());
        if (updatedUserEntity.getPassword() != null && !updatedUserEntity.getPassword().isEmpty()) {
            userEntity.setPassword(passwordEncoder.encode(updatedUserEntity.getPassword()));
        }
        userEntity.setRoles(updatedUserEntity.getRoles());
        userRepository.save(userEntity);
    }

    @Override
    public void deleteUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        // Delete associations in the user_roles table first
        userEntity.getRoles().clear();
        userRepository.save(userEntity);
        userRepository.delete(userEntity);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    @Cacheable(value = "users", key = "#userId")
    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
}