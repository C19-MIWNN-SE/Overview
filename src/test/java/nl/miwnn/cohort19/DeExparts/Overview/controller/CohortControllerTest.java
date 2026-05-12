package nl.miwnn.cohort19.DeExparts.Overview.controller;

import nl.miwnn.cohort19.DeExparts.Overview.configuration.overviewSecurityConfiguration;
import nl.miwnn.cohort19.DeExparts.Overview.model.*;
import nl.miwnn.cohort19.DeExparts.Overview.service.CohortService;
import nl.miwnn.cohort19.DeExparts.Overview.service.InstructorService;
import nl.miwnn.cohort19.DeExparts.Overview.service.ParticipantService;
import nl.miwnn.cohort19.DeExparts.Overview.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Coen Cuppes
 */
@Import(overviewSecurityConfiguration.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(CohortController.class)
public class CohortControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CohortService cohortService;

    @MockitoBean
    InstructorService instructorService;

    @MockitoBean
    ParticipantService participantService;

    @MockitoBean
    UserService userService;

    @MockitoBean
    PasswordEncoder passwordEncoder;

    private RequestPostProcessor testInstructorUser() {
        LocalDate date = LocalDate.now();
        User user = new User("testInstructor", "pw");
        Cohort cohort = new Cohort("TestCohort", "Testing", date, date);
        cohort.setId(1L);
        Instructor instructor = new Instructor(1L, "Jan", "Jansen",
                "jan@test.nl", "Groningen", "0612345678",
                "Beschrijving", "Mockito", date, user);
        instructor.setCohorts(List.of(cohort));
        user.setInstructor(instructor);
        Role role = new Role(1L, "ROLE_INSTRUCTOR", "instructor", List.of(user));
        user.setRoles(List.of(role));
        return user(user);
    }

    private RequestPostProcessor testParticipantUser(Long cohortId) {
        LocalDate date = LocalDate.now();
        User user = new User("testParticipant", "pw");
        Cohort cohort = new Cohort("TestCohort", "Testing", date, date);
        cohort.setId(cohortId);
        Participant participant = new Participant(1L, "Janine", "Jansens",
                "janine@test.nl", "Assen", "0623456789",
                "Beschrijving", date, "Sopra Steria", user);
        participant.setCohorts(cohort);
        user.setParticipant(participant);
        Role role = new Role(1L, "ROLE_PARTICIPANT", "participant", List.of(user));
        user.setRoles(List.of(role));
        return user(user);
    }

    @Test
    @DisplayName("showAllCohorts returns all cohorts in overview for Instructor")
    void showAllCohortsReturnsAllCohortsInOverviewForInstructor() throws Exception {
        Cohort cohort1 = new Cohort("Cohort 1", "Description", LocalDate.now(), LocalDate.now().plusMonths(5));
        Cohort cohort2 = new Cohort("Cohort 2", "Description", LocalDate.now(), LocalDate.now().plusMonths(5));
        List<Cohort> allCohorts = List.of(cohort1, cohort2);

        when(cohortService.showAllCohorts()).thenReturn(allCohorts);

        mockMvc.perform(get("/cohort/all")
                        .with(testInstructorUser()))
                .andExpect(status().isOk())
                .andExpect(view().name("overview-cohorts"))
                .andExpect(model().attribute("title", "Overzicht alle cohorten"))
                .andExpect(model().attribute("allCohorts", allCohorts));
    }

    @Test
    @DisplayName("showCohortDetails returns cohort view when participant accesses own cohort")
    void showCohortDetailsReturnsCohortViewWhenParticipantAccessesOwnCohort() throws Exception {
        Long cohortId = 1L;
        Cohort cohort = new Cohort("TestCohort", "Testing", LocalDate.now(), LocalDate.now());
        cohort.setId(cohortId);
        when(cohortService.findById(cohortId)).thenReturn(cohort);

        mockMvc.perform(get("/cohort/{id}", cohortId)
                        .with(testParticipantUser(cohortId)))
                .andExpect(status().isOk())
                .andExpect(view().name("detail-cohort"))
                .andExpect(model().attribute("cohort", cohort));

        verify(cohortService, times(1)).findById(cohortId);
    }

    @Test
    @DisplayName("showCohortDetails returns 403 view when Participant accesses other Cohort")
    void showCohortDetailsReturns403ViewWhenParticipantAccessesOtherCohort() throws Exception {
        mockMvc.perform(get("/cohort/2")
                        .with(testParticipantUser(1L)))
                .andExpect(status().isOk())
                .andExpect(view().name("error/403"));

        verify(cohortService, never()).findById(2L);
    }
}
