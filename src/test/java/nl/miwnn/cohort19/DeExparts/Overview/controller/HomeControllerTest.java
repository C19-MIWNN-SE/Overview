package nl.miwnn.cohort19.DeExparts.Overview.controller;

import nl.miwnn.cohort19.DeExparts.Overview.configuration.overviewSecurityConfiguration;
import nl.miwnn.cohort19.DeExparts.Overview.model.*;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.CohortRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.ImageRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.RoleRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.UserRepository;
import nl.miwnn.cohort19.DeExparts.Overview.service.CohortService;
import nl.miwnn.cohort19.DeExparts.Overview.service.InstructorService;
import nl.miwnn.cohort19.DeExparts.Overview.service.ParticipantService;
import nl.miwnn.cohort19.DeExparts.Overview.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.*;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author wat doe ik?
 */

@Import(overviewSecurityConfiguration.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {

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
    CohortRepository cohortRepository;

    @MockitoBean
    PasswordEncoder passwordEncoder;

    @MockitoBean
    private Instructor instructor;

    @MockitoBean
    private Participant participant;

    @MockitoBean
    private Cohort cohort;

    @MockitoBean
    private Collection<User> users;

    @MockitoBean
    private User testInstructor;

    @MockitoBean
    private User testParticipant;

    @MockitoBean
    private LocalDate date;

    @MockitoBean
    private Role instructorRole;

    private RequestPostProcessor testInstructorDetails(){
        System.out.println("Initializing setup");
        LocalDate date = LocalDate.now();
        Collection<User> users = new ArrayList<>();
        Cohort cohort = new Cohort("Cohort 1","Development",date,date);

        User testInstructor = new User("testInstructor","password");
        Instructor instructor = new Instructor(1L
                ,"Dirk"
                ,"de Vries"
                ,"mark.devries@example.com"
                ,"Rotterdam"
                ,"0612342222"
                ,"Beschrijving van instructor"
                ,"Wiskunde"
                ,date
                ,testInstructor);
        instructor.setCohorts(List.of(cohort));
        testInstructor.setInstructor(instructor);
        users.add(testInstructor);
        Role instructorRole = new Role(1L,"ROLE_INSTRUCTOR","instructor", users);
        testInstructor.setRoles(List.of(instructorRole));

        User testParticipant = new User("testParticipant","password");

        Participant participant = new Participant(1L
                ,"Clara"
                ,"de Groot"
                ,"clara.degroot@example.com"
                ,"Vlissingen"
                ,"0612342323"
                ,"Beschrijving van participant"
                ,date
                ,"Unive"
                ,testParticipant);
        participant.setCohorts(cohort);
        testParticipant.setParticipant(participant);
        users.add(testParticipant);
        Role participantRole = new Role(1L,"ROLE_PARTICIPANT","participant", users);
        testParticipant.setRoles(List.of(participantRole));

        return user(testInstructor);
    }

    private RequestPostProcessor testParticipantDetails(){
        LocalDate date = LocalDate.now();
        Collection<User> users = new ArrayList<>();
        Cohort cohort = new Cohort("Cohort 1","Development",date,date);
        List<Instructor> instructorList = new ArrayList<>();


        User testInstructor = new User("testInstructor","password");
        Instructor instructor = new Instructor(1L
                ,"Dirk"
                ,"de Vries"
                ,"mark.devries@example.com"
                ,"Rotterdam"
                ,"0612342222"
                ,"Beschrijving van instructor"
                ,"Wiskunde"
                ,date
                ,testInstructor);
        instructor.setCohorts(List.of(cohort));
        cohort.setInstructors(List.of(instructor));
        System.out.println("instructor in cohort"+cohort.getInstructors().get(0));
        testInstructor.setInstructor(instructor);
        users.add(testInstructor);
        Role instructorRole = new Role(1L,"ROLE_INSTRUCTOR","instructor", users);
        testInstructor.setRoles(List.of(instructorRole));

        User testParticipant = new User("testParticipant","password");

        Participant participant = new Participant(1L
                ,"Clara"
                ,"de Groot"
                ,"clara.degroot@example.com"
                ,"Vlissingen"
                ,"0612342323"
                ,"Beschrijving van participant"
                ,date
                ,"Unive"
                ,testParticipant);
        participant.setCohorts(cohort);
        System.out.println("cohortnaam van participant: " + participant.getCohorts().getName());
        System.out.println("cohort id van participant: " + participant.getCohorts().getId());


        testParticipant.setParticipant(participant);
        users.add(testParticipant);
        Role participantRole = new Role(1L,"ROLE_PARTICIPANT","participant", users);
        testParticipant.setRoles(List.of(participantRole));
        System.out.println("Cohort id van participant: " + testParticipant.getParticipant().getCohorts().getId());

        return user(testParticipant);
    }

    @Test
    void ReturnsHomePageCorrectlyForUserInstructor() throws Exception {

        mockMvc.perform(get("/home/")
                        .with(testInstructorDetails())
                        .requestAttr("user", instructor)
                        .requestAttr("userType","instructor"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("home"));
    }

    @Test
    void ReturnsHomePageCorrectlyForUserParticipant() throws Exception {

        when(participant.getCohorts()).thenReturn(cohort);
        when(cohortService.findById(1L)).thenReturn(cohort);
        when(cohort.getInstructors()).thenReturn(List.of(instructor));

        mockMvc.perform(get("/home/")
                        .with(testParticipantDetails())
                        .requestAttr("user", participant)
                        .requestAttr("userType","participant"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("home"));
    }
}
