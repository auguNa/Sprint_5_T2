package S5_T2.IT_ACADEMY.service.implementation;

import S5_T2.IT_ACADEMY.entity.UserEntity;
import S5_T2.IT_ACADEMY.repository.UserRepository;
import S5_T2.IT_ACADEMY.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserEntity getUserSettings(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserEntity> getAllUsers(String userDetails) {
        return userRepository.findAll();
    }

    public void createUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
    }

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

    public void deleteUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        // Delete associations in the user_roles table first
        userEntity.getRoles().clear(); // This will remove all roles associations with the user
        userRepository.save(userEntity); // Save the user entity to apply the changes
        userRepository.delete(userEntity);
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
}