package nl.miwnn.cohort19.DeExparts.Overview.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
@Configuration
@EnableWebSecurity
public class overviewSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/login",
                                "/images/**",
                                "/webjars/**",
                                "/css/**",
                                "/style.css"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/home/",
                                "/detail/aboutme",
                                "/cohort/",
                                "/cohort/*",
                                "/detail/participant/*",
                                "/detail/instructor/*"
                        ).hasAnyRole("PARTICIPANT", "INSTRUCTOR")

                        .requestMatchers(
                                "/cohort/add",
                                "/cohort/edit/*",
                                "/cohort/delete/*",

                                "/detail/instructor/add",
                                "/detail/instructor/edit/*",
                                "/detail/instructor/save",
                                "/detail/instructor/delete/*",

                                "/detail/participant/add",
                                "/detail/participant/edit/*",
                                "/detail/participant/save",
                                "/detail/participant/delete/*"
                        ).hasRole("INSTRUCTOR")

                        .requestMatchers(
                                "/detail/participant/edit/*",
                                "/detail/participant/save"
                        ).hasRole("PARTICIPANT")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}