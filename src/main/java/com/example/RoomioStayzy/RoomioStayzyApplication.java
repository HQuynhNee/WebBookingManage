package com.example.RoomioStayzy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import com.example.RoomioStayzy.entities.User;
import com.example.RoomioStayzy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class RoomioStayzyApplication {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(RoomioStayzyApplication.class, args);
	}

	@Bean
	public CommandLineRunner createAdminAccount() {
		return args -> {
			if (userRepository.findByUsername("admin") == null) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin"));
				admin.setEmail("admin@example.com");
				admin.setPhone("123456783");
				admin.setRole(User.Role.ADMIN);
				userRepository.save(admin);
				System.out.println("Admin account created!");
			} else {
				System.out.println("Admin account already exists.");
			}
		};
	}
}

