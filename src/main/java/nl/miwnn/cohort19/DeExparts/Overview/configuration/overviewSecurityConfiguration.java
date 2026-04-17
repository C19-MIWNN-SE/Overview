package nl.miwnn.cohort19.DeExparts.Overview.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
@Configuration
@EnableWebSecurity
public class overviewSecurityConfiguration {

    private static final Logger log =
            LoggerFactory.getLogger(overviewSecurityConfiguration.class);

    @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/",
                                    "/home/**",
                                    "/images/**",
                                    "/login/**",
                                    "/overview/**",
                                    "/css/**",
                                    "/webjars/**").permitAll()
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

        @Bean
        public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//            String password = UUID.randomUUID().toString();
//            log.info("==========================================================================");
//            log.info("Generated password: {}", password);
//            log.info("==========================================================================");
            var deelnemer = User.builder()
                    .username("deelnemer")
                    .password(encoder.encode("deelnemer"))
                    .roles("DEELNEMER")
                    .build();
            var docent = User.builder()
                    .username("docent")
                    .password(encoder.encode("docent"))
                    .roles("DOCENT")
                    .build();
            var beheerder = User.builder()
                    .username("beheerder")
                    .password(encoder.encode("beheerder"))
                    .roles("ADMIN")
                    .build();
            return new InMemoryUserDetailsManager(deelnemer, docent, beheerder);
        }
    }
