package nl.miwnn.cohort19.DeExparts.Overview.configuration;

import nl.miwnn.cohort19.DeExparts.Overview.configuration.overviewSecurityConfiguration;
import nl.miwnn.cohort19.DeExparts.Overview.controller.*;
import nl.miwnn.cohort19.DeExparts.Overview.model.*;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.ImageRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.RoleRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.UserRepository;
import nl.miwnn.cohort19.DeExparts.Overview.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.*;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author wat doe ik?
 */

@Import(overviewSecurityConfiguration.class)
@WebMvcTest({HomeController.class,
            AboutMeController.class,
            CohortController.class,
            InstructorController.class,
            LoginController.class,
            ParticipantController.class})
public class SecurityConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    CohortService cohortService;

    @MockitoBean
    ParticipantService participantService;

    @MockitoBean
    InstructorService instructorService;

    @MockitoBean
    UserService userService;

    @MockitoBean
    ImageRepository imageRepository;

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    RoleRepository roleRepository;

    @MockitoBean
    PasswordEncoder passwordEncoder;

    @MockitoBean
    private User testInstructor;

    @MockitoBean
    private User testParticipant;



    //NO BEFOREEACH, AS THAT DOESN'T SEEM TO WORK AT ALL WITH THESE TESTS.
    private RequestPostProcessor testInstructorDetails(){
        Collection<User> users = new ArrayList<>();
        User testInstructor = new User("testInstructor","password");
        users.add(testInstructor);
        Role instructorRole = new Role(1L,"ROLE_INSTRUCTOR","instructor", users);
        testInstructor.setRoles(List.of(instructorRole));

        return user(testInstructor);
    }

    private RequestPostProcessor testParticipantDetails(){
        Collection<User> users = new ArrayList<>();
        User testParticipant = new User("testParticipant","password");
        users.add(testParticipant);
        Role participantRole = new Role(1L,"ROLE_PARTICIPANT","participant", users);
        testParticipant.setRoles(List.of(participantRole));

        return user(testParticipant);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/home",
            "/cohort/",
            "/cohort/add",
            "/cohort/edit/*",
            "/cohort/delete/*",

            "/detail/instructor/add",
            "/detail/instructor/edit/*",
            "/detail/instructor/save",
            "/detail/instructor/delete/*",

            "/detail/participant/add",
            "/detail/participant/delete/*",

//ABOUTME GIVES THYMELEAFEXCEPTION WHEN NO THYMELEAF REQUEST IS MADE
            //"/detail/aboutme",

            "/detail/participant/*",
            "/detail/instructor/*",

            "/detail/participant/edit/*",
            "/detail/participant/save"})
    void UserInstructorHasAccessToAllPages(String string) throws Exception{

        mockMvc.perform(get(URI.create(string))
                .with(testInstructorDetails()))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/cohort/",
            "/cohort/add",
            "/cohort/edit/*",
            "/cohort/delete/*",

            "/detail/instructor/add",
            "/detail/instructor/edit/*",
            "/detail/instructor/save",
            "/detail/instructor/delete/*",

            "/detail/participant/add",
            "/detail/participant/delete/*"})
    void UserParticipantDeniedFromPages(String string) throws Exception{
        mockMvc.perform(get(URI.create(string))
                        .with(testParticipantDetails()))
                        .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            //"/home/",
            //"/detail/aboutme", DEZE VRAGEN ALLEBEI OM THYMELEAF GEGEVENS DIE ZE NIET NODIG ZOUDEN MOETEN HEBBEN

            "/cohort/*",
            "/detail/participant/*",
            "/detail/instructor/*",
            "/detail/participant/edit/*",
            "/detail/participant/save"})
    void UserParticipantHasAccessToPages(String string) throws Exception{
        mockMvc.perform(get(URI.create(string))
                .with(testParticipantDetails()))
                .andExpect(status().isOk());
    }
}

