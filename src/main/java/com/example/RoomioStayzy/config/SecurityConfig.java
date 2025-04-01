    package com.example.RoomioStayzy.config;

    import com.example.RoomioStayzy.services.CustomUserDetailsService;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.web.authentication.AuthenticationFailureHandler;
    import jakarta.servlet.Filter;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {
        //lấy thông tin người dùng từ database
        private final CustomUserDetailsService customUserDetailsService;

        public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
            this.customUserDetailsService = customUserDetailsService;
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return customUserDetailsService;
        }

        @Bean


        // Phân quyền truy cập cho các trang và API

        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable) // chống giả mạo yêu cầu từ trình duyệt (Bật lại nếu dùng form POST )
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/login", "/register", "/css/**", "/img/**", "/js/**").permitAll()
                            .requestMatchers("/house/add", "/house/edit").hasAnyAuthority("ADMIN", "OWNER") // Restricted to ADMIN and OWNER
                            .requestMatchers("/house/detail").authenticated() // All authenticated users can access                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                            .requestMatchers("/owner/**").hasAuthority("OWNER")
                            .requestMatchers("/student/**").hasAuthority("STUDENT")
                            .requestMatchers("/admin/**").hasAuthority("ADMIN")
    //                        .requestMatchers("/api/**").authenticated()
                            .anyRequest().authenticated()
                    )

                    //đăng nhập và xử lý sau đăng nhập

                    .formLogin(form -> form
                            .loginPage("/login")
                            .successHandler((request, response, authentication) -> {
                                if (authentication.getAuthorities().stream()
                                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {
                                    response.sendRedirect("/admin");
                                } else if (authentication.getAuthorities().stream()
                                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("OWNER"))) {
                                    response.sendRedirect("/owner");
                                } else if (authentication.getAuthorities().stream()
                                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("STUDENT"))) {
                                    response.sendRedirect("/home");
                                } else {
                                    response.sendRedirect("/home");
                                }
                            })
                            .failureHandler(authenticationFailureHandler())
                            .permitAll()
                    )
                    .logout(logout -> logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login?logout")
                            .permitAll()
                    );

            return http.build();
        }
        @Bean(name = "customRoleRedirectFilter")
        public Filter roleRedirectFilter() {
            return new RoleRedirectFilter();
        }


        @Bean

        // Xử lý lỗi khi đăng nhập sai

        public AuthenticationFailureHandler authenticationFailureHandler() {
            return (request, response, exception) -> {
                String errorMessage = exception instanceof UsernameNotFoundException ?
                        "User not found" : "Invalid credentials";
                response.sendRedirect("/login?error=" + errorMessage);
            };
        }


        @Bean
        // Mã hóa mật khẩu người dùng để bảo mật trong khoảng 14 vòng
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder(14);
        }

        @Bean
        // Quản lý quá trình xác thực người dùng
        public  AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return  authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        // Cấu hình cách xác thực người dùng
        public AuthenticationProvider authenticationProvider(){
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setUserDetailsService(customUserDetailsService);
            provider.setPasswordEncoder(bCryptPasswordEncoder());
            return provider;
        }
    }
