package S5_T2.IT_ACADEMY.controller;

import S5_T2.IT_ACADEMY.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        authService.registerUser(username, password);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return authService.loginUser(username, password);
    }
}