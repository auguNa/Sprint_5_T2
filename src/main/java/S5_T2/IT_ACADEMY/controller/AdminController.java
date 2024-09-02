package S5_T2.IT_ACADEMY.controller;

import S5_T2.IT_ACADEMY.dto.AuthRequest;
import S5_T2.IT_ACADEMY.entity.UserEntity;
import S5_T2.IT_ACADEMY.service.AuthService;
import S5_T2.IT_ACADEMY.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AuthRequest request) {
        try {
            authService.registerAdmin(request.getUsername(), request.getPassword(), request.getRoles());
            return ResponseEntity.ok("Admin registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin registration failed: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<UserEntity> userEntities = userService.getAllUsers(userDetails.getUsername());
        return ResponseEntity.ok(userEntities);
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserEntity userEntity) {
        try {
            userService.createUser(userEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User creation failed: " + e.getMessage());
        }
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody UserEntity userEntity) {
        try {
            userService.updateUser(userId, userEntity);
            return ResponseEntity.ok("User updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User update failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User deletion failed: " + e.getMessage());
        }
    }
}
