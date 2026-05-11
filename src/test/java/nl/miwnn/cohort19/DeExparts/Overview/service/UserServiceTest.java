package nl.miwnn.cohort19.DeExparts.Overview.service;

import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.model.User;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * Author: Anouk de Vos
 * verifies that loadUserByUsername returns the correct user
 * when the user exists, and throws a UsernameNotFoundException when the user does not exist.
 */

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("loadUserByUsername throws exception when not exist")
    void loadUserByUsernameThrowsExceptionWhenNotExist() {
        when(userRepository.findByUsername("onbekend")).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () ->
                userService.loadUserByUsername("onbekend"));
    }

    @Test
    @DisplayName("loadUserByUsername returns user when exists")
    void loadUserByUsernameReturnsUserWhenExists() {
        User testUser = new User("testuser", "password");
        testUser.setRoles(new ArrayList<>());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        UserDetails result = userService.loadUserByUsername("testuser");

        Assertions.assertEquals("testuser", result.getUsername());

    }
}

