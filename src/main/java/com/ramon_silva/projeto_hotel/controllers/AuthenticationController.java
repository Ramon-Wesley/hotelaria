package com.ramon_silva.projeto_hotel.controllers;


import org.springframework.http.HttpStatus;
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
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    
    public AuthenticationController(AuthenticationManager authenticationManager,UserRepository userRepository,TokenService tokenService){
        this.authenticationManager=authenticationManager;
        this.userRepository=userRepository;
        this.tokenService=tokenService;
    }
    
    @PostMapping
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticatedDto data){
        var usernamePassword= new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
        var auth=this.authenticationManager.authenticate(usernamePassword);

        var token=tokenService.GeneratedToken((UsersModel)auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> register(@RequestBody @Valid UsersDto data){
        if(this.userRepository.findByLogin(data.getLogin()) != null){
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }else{
            String hashPass= new BCryptPasswordEncoder().encode(data.getPassword());
            
            UsersModel user= new UsersModel(data.getLogin(),hashPass,data.getRole());
            this.userRepository.save(user);
            return ResponseEntity.ok().build();
        }
    }
}
