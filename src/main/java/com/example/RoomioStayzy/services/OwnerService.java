package com.example.RoomioStayzy.services;

import com.example.RoomioStayzy.entities.User;
import com.example.RoomioStayzy.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class OwnerService {
    private final UserRepository userRepository;
    public OwnerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Optional<User> getUserById(Long Id){
        return userRepository.findById(Id);
    }
}
