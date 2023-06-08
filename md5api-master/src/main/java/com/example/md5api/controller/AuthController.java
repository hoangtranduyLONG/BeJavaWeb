package com.example.md5api.controller;

import com.example.md5api.model.ERole;
import com.example.md5api.model.Role;
import com.example.md5api.model.TheUser;
import com.example.md5api.payload.request.LoginRequest;
import com.example.md5api.payload.request.SignupRequest;
import com.example.md5api.payload.response.JwtResponse;
import com.example.md5api.payload.response.MessageResponse;
import com.example.md5api.repository.IRoleRepository;
import com.example.md5api.repository.IUserRepository;
import com.example.md5api.security.jwt.JwtUtils;
import com.example.md5api.security.service.UserDetailImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IRoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailImp userDetailImp = (UserDetailImp) authentication.getPrincipal();
        List<String> role = userDetailImp.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetailImp.getId(),
                userDetailImp.getUsername(),
                userDetailImp.getEmail(),
                role));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username taken"));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email in Use"));
        }

        TheUser user = new TheUser(signupRequest.getUsername(), signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));
        Set<String> strRole = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRole == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found"));
            roles.add(userRole);
        } else {
            strRole.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(adminRole);
                        break;

                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered"));
    }
}
