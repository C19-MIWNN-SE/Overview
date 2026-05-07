package nl.miwnn.cohort19.DeExparts.Overview.configuration;

import jakarta.servlet.Filter;
import nl.miwnn.cohort19.DeExparts.Overview.controller.CohortController;
import nl.miwnn.cohort19.DeExparts.Overview.controller.HomeController;
import nl.miwnn.cohort19.DeExparts.Overview.controller.LoginController;
import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.model.User;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.InstructorRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.RoleRepository;
import nl.miwnn.cohort19.DeExparts.Overview.service.CohortService;
import nl.miwnn.cohort19.DeExparts.Overview.service.InstructorService;
import nl.miwnn.cohort19.DeExparts.Overview.service.ParticipantService;
import nl.miwnn.cohort19.DeExparts.Overview.service.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import nl.miwnn.cohort19.DeExparts.Overview.model.Role;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.*;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.*;
import org.springframework.test.context.bean.override.mockito.MockitoBeans;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author wat doe ik?
 */

@Import({overviewSecurityConfiguration.class})

@WebMvcTest(HomeController.class)
public class overviewSecurityConfigurationTest {

    private User testInstructor;
    private User testParticipant;
    private Role participantRole;
    private Role instructorRole;

    @MockitoBean
    RoleRepository roleRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    overviewSecurityConfiguration securityConfiguration;

    @MockitoBean
    CohortService cohortService;

    @MockitoBean
    ParticipantService participantService;

    @MockitoBean
    InstructorService instructorService;

    @MockitoBean
    UserService userService;

    @MockitoBean
    InstructorRepository instructorRepository;



//    @BeforeEach
//    void setUp(){
//        instructorRole = roleRepository.findByAuthority("ROLE_INSTRUCTOR").orElseThrow();
//        testInstructor = new User();
//        testInstructor.setRoles(List.of(instructorRole));
//
//        participantRole = roleRepository.findByAuthority("ROLE_PARTICIPANT").orElseThrow();
//        testParticipant = new User();
//        testParticipant.setRoles(List.of(participantRole));
//    }


    @Test
    void ReturnsHomePageCorrectlyForUserInstructor() throws Exception {
        User testInstructor = new User("test","password");
        LocalDate date = LocalDate.now();
        Instructor instructor = new Instructor(1L,"Dirk","de Vries","mark.devries@example.com","Rotterdam","0612342222","Beschrijving van instructor","Wiskunde",date,testInstructor);
        testInstructor.setInstructor(instructor);
        Collection<User> users = new ArrayList<>();
        Role instructorRole = new Role(1L,"ROLE_INSTRUCTOR","instructor", users);
        testInstructor.setRoles(List.of(instructorRole));
        System.out.println("Volledige naam = " + testInstructor.getInstructor().getFullName());


        mockMvc.perform(get("/home/").with(user(testInstructor)).requestAttr("user", instructor).requestAttr("userType","instructor"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }
}
