package S5_T2.IT_ACADEMY;

import S5_T2.IT_ACADEMY.entity.UserEntity;
import S5_T2.IT_ACADEMY.repository.UserRepository;
import S5_T2.IT_ACADEMY.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("testUser");
        userEntity.setPassword("password");
        userEntity.setRoles(new HashSet<>());
    }


    @Test
    void testFindByUsername_CacheBehavior() {
        String username = "testUser";

        // Mock repository to return the user entity
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        // First call: should hit the DB
        UserEntity result = userService.findByUsername(username);
        verify(userRepository, times(1)).findByUsername(username);
        assertEquals(userEntity, result);

        // Second call: should hit the cache, no DB call
        result = userService.findByUsername(username);
        verify(userRepository, times(1)).findByUsername(username); // No additional DB call
        assertEquals(userEntity, result);
    }

    @Test
    void testFindById_CacheBehavior() {
        Long userId = 1L;

        // Mock repository to return the user entity
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // First call: should hit the DB
        UserEntity result = userService.findById(userId);
        verify(userRepository, times(1)).findById(userId);
        assertEquals(userEntity, result);

        // Second call: should hit the cache, no DB call
        result = userService.findById(userId);
        verify(userRepository, times(1)).findById(userId); // No additional DB call
        assertEquals(userEntity, result);
    }

    @Test
    void testCreateUser() {
        String rawPassword = "rawPassword";
        String encodedPassword = "encodedPassword";
        userEntity.setPassword(rawPassword);

        // Mock password encoding
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // Call the method
        userService.createUser(userEntity);

        // Verify the password was encoded
        assertEquals(encodedPassword, userEntity.getPassword());

        // Verify the user was saved in the repository
        verify(userRepository, times(1)).save(userEntity);

        // Verify the user was cached
        assertEquals(userEntity, userService.findByUsername(userEntity.getUsername()));
        assertEquals(userEntity, userService.findById(userEntity.getId()));
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        UserEntity updatedUserEntity = new UserEntity();
        updatedUserEntity.setUsername("updatedUser");
        updatedUserEntity.setPassword("newPassword");

        // Mock repository
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.encode(updatedUserEntity.getPassword())).thenReturn("encodedNewPassword");

        // Call the update method
        userService.updateUser(userId, updatedUserEntity);

        // Verify user details were updated and saved
        assertEquals("updatedUser", userEntity.getUsername());
        assertEquals("encodedNewPassword", userEntity.getPassword());
        verify(userRepository, times(1)).save(userEntity);

        // Verify cache was updated
        assertEquals(userEntity, userService.findByUsername(userEntity.getUsername()));
        assertEquals(userEntity, userService.findById(userEntity.getId()));
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;

        // Mock repository
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // Call the delete method
        userService.deleteUser(userId);

        // Verify the user was deleted from the repository
        verify(userRepository, times(1)).delete(userEntity);

        // Verify cache was invalidated
        assertNull(userService.userCacheById.get(userId));
        assertNull(userService.userCacheByUsername.get(userEntity.getUsername()));
    }

    @Test
    void testGetUserSettings_UserNotFound() {
        String username = "unknownUser";

        // Mock repository to return an empty result
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Verify that an exception is thrown when the user is not found
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserSettings(username);
        });
        assertEquals("User not found", exception.getMessage());
    }
}
