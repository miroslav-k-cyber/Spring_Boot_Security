package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final SuccessUserHandler successUserHandler;
    private final UserDetailsService userDetailsService;
    
    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserDetailsService userDetailsService) {
        this.successUserHandler = successUserHandler;
        this.userDetailsService = userDetailsService;
    }
    
    //  1(Цепочка фильтров)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 1. Настраиваем патрули на URL (Пункты 4 и 5 ТЗ)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Админка только для ADMIN
                        .requestMatchers("/user").hasAnyRole("USER", "ADMIN") // Страница юзера для USER и ADMIN
                        .requestMatchers("/", "/index").permitAll() // Главная открыта всем
                        .anyRequest().authenticated() // Всё остальное требует логина
                )
                // 2. Настраиваем форму логина и редирект (Пункт 7 ТЗ)
                .formLogin(form -> form
                        .usernameParameter("username") // Фикс: явно связываем стандартное поле формы с обработчиком
                        .successHandler(successUserHandler) // Наш хэндлер перенаправления
                        .permitAll()
                )
                // 3. Настраиваем выход из аккаунта (Пункт 6 ТЗ)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login") // Куда отправить после выхода
                        .permitAll()
                )
                .authenticationProvider(daoAuthenticationProvider(userDetailsService))
                // 4. Финальная сборка всей цепочки фильтров
                .build();
    }
    
    // 2 (Провайдер базы данных)
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService); // Передаем сервис для работы с БД
        return provider;
    }
    
    // 3 (Шифровальщик паролей)
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Для pre-project используем NoOp (без шифрования), как просит ТЗ
        return NoOpPasswordEncoder.getInstance();
    }
}

