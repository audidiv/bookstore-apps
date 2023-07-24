package com.audidiv.library.controller;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.audidiv.library.config.JWTGenerator;
import com.audidiv.library.dto.request.RequestLoginDto;
import com.audidiv.library.dto.request.RequestRegisterDto;
import com.audidiv.library.dto.response.ResponseAuthDTO;
import com.audidiv.library.model.Role;
import com.audidiv.library.model.UserEntity;
import com.audidiv.library.repository.RoleRepository;
import com.audidiv.library.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JWTGenerator jwtGenerator;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTGenerator jwtGenerator) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("user/register")
    public ResponseEntity<String> register(@RequestBody RequestRegisterDto request){
        System.out.println(request.getUsername());
        System.out.println(request.getEmail());
        System.out.println(request.getPhoneNumber());
        if(userRepository.existsByUsername(request.getUsername())){
            return new ResponseEntity<String>("Username has already registered", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(request.getEmail())){
            return new ResponseEntity<String>("Email has already registered", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByPhoneNumber(request.getPhoneNumber())){
            return new ResponseEntity<String>("Phone Number has already registered", HttpStatus.BAD_REQUEST);
        }

        UserEntity newUser = new UserEntity();
        newUser.setFullName(request.getFullName());
        newUser.setEmail(request.getEmail());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        Role roles = roleRepository.findByName("USER").get();
        newUser.setRoles(Collections.singletonList(roles));

        userRepository.save(newUser);

        return new ResponseEntity<String>("User Register Success", HttpStatus.OK);
    }

    @PostMapping("admin/register")
    public ResponseEntity<String> adminRegister(@RequestBody RequestRegisterDto request){
        if(userRepository.existsByUsername(request.getUsername())){
            return new ResponseEntity<String>("Username has already registered", HttpStatus.BAD_REQUEST);
        }

        UserEntity newAdmin = new UserEntity();
        newAdmin.setFullName(request.getFullName());
        newAdmin.setEmail(request.getEmail());
        newAdmin.setPhoneNumber(request.getPhoneNumber());
        newAdmin.setUsername(request.getUsername());
        newAdmin.setPassword(passwordEncoder.encode(request.getPassword()));

        Role roles = roleRepository.findByName("ADMIN").get();
        newAdmin.setRoles(Collections.singletonList(roles));

        userRepository.save(newAdmin);

        return new ResponseEntity<String>("Admin Register Success", HttpStatus.OK);
    }

    @PostMapping("user/login")
    public ResponseEntity<ResponseAuthDTO> login(@RequestBody RequestLoginDto request){
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<ResponseAuthDTO>(new ResponseAuthDTO(token), HttpStatus.OK);
    }
}
