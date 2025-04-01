package com.example.RoomioStayzy.controllers;

import com.example.RoomioStayzy.DTO.UserDTO;
import com.example.RoomioStayzy.entities.User;
import com.example.RoomioStayzy.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    // Khai báo
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;

    }

    //login để xử lý logic đăng nhập
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "auth/login";
    }
    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            RedirectAttributes redirectAttributes,
                            Model model) {
       return "auth/login";
    }
    //register để xử lý đăng ký tài khoản
    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDTO form, Model model) {
        System.out.println("Here");

        // Kiểm tra mật khẩu và xác nhận mật khẩu có trùng khớp hay không
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("confirmPassword", "Passwords do not match.");
        }

        // Kiểm tra username trong database
        if (userRepository.findByUsername(form.getUsername()) == null) {
            model.addAttribute("username",  "Username is already taken.");
        }

        String encodedPassword = passwordEncoder.encode(form.getPassword());

        // Tạo đối tượng User để lưu vào database
        User user = new User();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setPhone(form.getPhone());
        user.setPassword(encodedPassword);
        if ("OWNER".equalsIgnoreCase(form.getRole())) {
            user.setRole(User.Role.OWNER);
        } else {
            user.setRole(User.Role.STUDENT);
        }
        userRepository.save(user);


        return "redirect:/login"; // Chuyển hướng về trang login sau khi đăng ký thành công
    }

    // logout để xử lý đăng xuất
    @PostMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/login?logout";
    }

}
