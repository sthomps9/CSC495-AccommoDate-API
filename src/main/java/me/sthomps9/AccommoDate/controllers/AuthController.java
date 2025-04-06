package me.sthomps9.AccommoDate.controllers;

import me.sthomps9.AccommoDate.dao.UserDAO;
import me.sthomps9.AccommoDate.jwt.JwtResponse;
import me.sthomps9.AccommoDate.jwt.JwtUtil;
import me.sthomps9.AccommoDate.model.User;
import me.sthomps9.AccommoDate.repository.UserRepository;
import me.sthomps9.AccommoDate.request.AuthRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserDAO userDao;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userDao, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userDao = userDao.getDao();
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        Optional<User> loggingIn = userDao.findByEmail(authRequest.getEmail());
        System.out.println("Login request: " + authRequest.getEmail() + " / " + authRequest.getPassword());

        if (loggingIn.isPresent()) {
            User user = loggingIn.get();
            System.out.println(user.getEmail());
            System.out.println(user.getPwd());
            System.out.println(user.getFullname());
            System.out.println(user.getPreferredname());
            System.out.println(user.getStudentid());
            System.out.println(user.getRole());
            System.out.println(user.getTitle());

            if (passwordEncoder.matches(authRequest.getPassword(), user.getPwd())) {

                String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
                return ResponseEntity.ok(new JwtResponse(token));
            }
        }

        return ResponseEntity.status(401).body("Invalid username or password");
    }
}


