package com.example.food.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // 정적 리소스 허용
                .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자 페이지
                .requestMatchers("/login", "/join", "/agreement", "/login/findId", "/login/findPassword", "/main").permitAll() // 비인증 사용자 접근 허용
                .anyRequest().authenticated() // 나머지 요청은 인증 필요
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/main/main", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/mail/**") // 특정 경로에 대해 CSRF 비활성화
            );

        return http.build(); // HttpSecurity 객체 빌드
    }
}
