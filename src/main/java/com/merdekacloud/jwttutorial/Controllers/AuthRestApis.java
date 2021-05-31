package com.merdekacloud.jwttutorial.Controllers;

import com.merdekacloud.jwttutorial.Config.JwtTokenProvider;
import com.merdekacloud.jwttutorial.Models.*;
import com.merdekacloud.jwttutorial.Repositories.RoleRepository;
import com.merdekacloud.jwttutorial.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins="*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestApis {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signin")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity registerUser(@Valid @RequestBody SignupForm signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return new ResponseEntity("Fail -> User name already taken", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return new ResponseEntity("Fail -> email already exist", HttpStatus.BAD_REQUEST);
        }

        User user = new User(signupRequest.getName(), signupRequest.getUsername(), signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));

        Set strRoles = signupRequest.getRole();

        Set roles = new HashSet<>();

        strRoles.forEach(role ->{
            switch(role.toString()){
                case "admin":
                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Fail -> User role cannot be found"));
                    roles.add(adminRole);
                    break;
                case "pm":
                    Role pmRole =  roleRepository.findByName(RoleName.ROLE_PM).orElseThrow(() -> new RuntimeException("Fail -> User role cannot be found"));
                    roles.add(pmRole);
                    break;
                default:
                    Role userRole =  roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new RuntimeException("Fail -> User role cannot be found"));
                    roles.add(userRole);
                    break;
            }
        });

        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.ok().body("User has been registered successfully");

    }
}
