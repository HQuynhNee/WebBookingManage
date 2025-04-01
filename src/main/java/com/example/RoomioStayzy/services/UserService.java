package com.example.RoomioStayzy.services;

import com.example.RoomioStayzy.entities.User;
import com.example.RoomioStayzy.repositories.FacilityRepository;
import com.example.RoomioStayzy.repositories.HousingRepository;
import com.example.RoomioStayzy.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class UserService {
    private final UserRepository userRepository;
    private  final AuthenticationManager authenticationManager;
    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }
    public User getUserById(Long userId){
        return userRepository.getReferenceById(userId);
    }
    public boolean verify(User user){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        return authentication.isAuthenticated();
    }

}
