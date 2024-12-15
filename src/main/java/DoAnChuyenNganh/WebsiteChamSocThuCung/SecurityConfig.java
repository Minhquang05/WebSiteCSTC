package DoAnChuyenNganh.WebsiteChamSocThuCung.config;

import DoAnChuyenNganh.WebsiteChamSocThuCung.security.CustomerOauth2UserService;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserService userService;
    private final CustomerOauth2UserService oauth2UserService; // Đưa vào constructor

    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService());
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/", "/oauth/**", "/register", "/error",
                                "/products", "/cart", "/cart/**", "/order/checkout", "order/submit", "order/confirmation", "/home", "/home/**", "/error/**",
                                "/products/details/**", "/images/**")
                        .permitAll()
                        .requestMatchers("/products/edit/**", "/products/add", "/products/delete/**", "/categories",
                                "/categories/add", "/categories/edit/**", "/categories/update/**", "/categories/delete/**",
                                "/employees", "/order/updateStatus/**", "/order/admin")
                        .hasAnyAuthority("ADMIN")
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("hutech")
                        .rememberMeCookieName("hutech")
                        .tokenValiditySeconds(24 * 60 * 60)
                        .userDetailsService(userDetailsService())
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(oauth2UserService) // Sử dụng oauth2UserService đã được inject
                        )
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/access-denied")
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .maximumSessions(1)
                        .expiredUrl("/login")
                )
                .httpBasic(httpBasic -> httpBasic
                        .realmName("hutech")
                )
                .build();
    }
}