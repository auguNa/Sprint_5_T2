package S5_T2.IT_ACADEMY.service;

import S5_T2.IT_ACADEMY.entity.UserEntity;
import S5_T2.IT_ACADEMY.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Retrieve user settings by username
    public UserEntity getUserSettings(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Retrieve all users
    public List<UserEntity> getAllUsers(String userDetails) {
        return userRepository.findAll();
    }

    // Create a new user
    public void createUser(UserEntity userEntity) {
        // Encode password before saving
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
    }

    // Update existing user
    public void updateUser(Long userId, UserEntity updatedUserEntity) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Update user details
        userEntity.setUsername(updatedUserEntity.getUsername());
        if (updatedUserEntity.getPassword() != null && !updatedUserEntity.getPassword().isEmpty()) {
            userEntity.setPassword(passwordEncoder.encode(updatedUserEntity.getPassword()));
        }
        // Update roles if needed
        userEntity.setRoles(updatedUserEntity.getRoles());

        userRepository.save(userEntity);
    }

    // Delete user by ID
    public void deleteUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Delete associations in the user_roles table first
        userEntity.getRoles().clear(); // This will remove all roles associations with the user

        userRepository.save(userEntity); // Save the user entity to apply the changes
        userRepository.delete(userEntity);
    }

    // Retrieve user by username
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
}
