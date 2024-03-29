package com.ramon_silva.projeto_hotel.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ramon_silva.projeto_hotel.repositories.UserRepository;

@Service
public class UserServiceIMP implements UserDetailsService {

    private final UserRepository userRepository;
    
    private UserServiceIMP(UserRepository userRepository){
      this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      return userRepository.findByLogin(username);
    }
    
}
