package com.ramon_silva.projeto_hotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ramon_silva.projeto_hotel.dto.AuthenticatedDto;
import com.ramon_silva.projeto_hotel.dto.UsersDto;
import com.ramon_silva.projeto_hotel.dto.LoginResponseDto;
import com.ramon_silva.projeto_hotel.infra.security.TokenService;
import com.ramon_silva.projeto_hotel.models.UsersModel;
import com.ramon_silva.projeto_hotel.repositories.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class AuthenticationController {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    TokenService tokenService;
    
    @PostMapping
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticatedDto data){
        var usernamePassword= new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth=this.authenticationManager.authenticate(usernamePassword);

        var token=tokenService.GeneratedToken((UsersModel)auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> register(@RequestBody @Valid UsersDto data){
        if(this.userRepository.findByLogin(data.login()) != null){
            return ResponseEntity.badRequest().build();
        }else{
            String hashPass= new BCryptPasswordEncoder().encode(data.password());
            
            UsersModel user= new UsersModel(data.login(),hashPass,data.role());
            this.userRepository.save(user);
            return ResponseEntity.ok().build();
        }
    }
}