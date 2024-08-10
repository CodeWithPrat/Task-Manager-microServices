package com.taskManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskManager.config.JwtProvider;
import com.taskManager.modal.User;
import com.taskManager.repository.UserRepository;
import com.taskManager.request.LoginRequest;
import com.taskManager.response.AuthResponse;
import com.taskManager.service.CustomerUserServiceImplimentation;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerUserServiceImplimentation customerDetails;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String role = user.getRole();

        User isEmailExist = userRepository.findByEmail(email);

        if (isEmailExist != null) {
            throw new Exception("Email already exists");
        }

        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFullName(fullName);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setRole(role);
        User savedUser = userRepository.save(createdUser);

        // Creating an authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(), savedUser.getPassword(), savedUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generating JWT token
        String token = JwtProvider.generateToken(authentication);

        // Creating response object
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Registration Successful");
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication); // Updated this line
        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Login Successful");
        authResponse.setJwt(token);
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    // Custom authentication class
    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customerDetails.loadUserByUsername(username);
        System.out.println("sign in userdetails- " + userDetails);

        if (userDetails == null) {
            System.out.println("signin userdetails - null" + userDetails);
            throw new BadCredentialsException("Invalid Username or Password");

        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            System.out.println("signin userdetails - password not match" + userDetails);
            throw new BadCredentialsException("Invalid Username or Password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
