package S5_T2.IT_ACADEMY.controller;

import S5_T2.IT_ACADEMY.dto.AuthRequest;
import S5_T2.IT_ACADEMY.entity.UserEntity;
import S5_T2.IT_ACADEMY.exception.UserAlreadyExistsException;
import S5_T2.IT_ACADEMY.service.AuthService;
import S5_T2.IT_ACADEMY.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@Slf4j
@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        try {
            authService.registerUser(authRequest.getUsername(), authRequest.getPassword(), authRequest.getRoles());
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (UserAlreadyExistsException e) {
            log.error("Registration failed: User already exists", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error during registration: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during registration");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        try {
            String token = authService.loginUser(authRequest.getUsername(), authRequest.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            log.error("Error during login: ", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserEntity> getAuthenticatedUser(Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserEntity userEntity = userService.findByUsername(userDetails.getUsername());
            return ResponseEntity.ok(userEntity);
        } catch (Exception e) {
            log.error("Error fetching authenticated user details: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}