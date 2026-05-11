package nl.miwnn.cohort19.DeExparts.Overview.service;

import nl.miwnn.cohort19.DeExparts.Overview.model.Cohort;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.model.User;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.ParticipantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author wat doe ik?
 */
@ExtendWith(MockitoExtension.class)
public class ParticipantServiceTest {

    @Mock
    ParticipantRepository participantRepository;

    @InjectMocks
    ParticipantService participantService;

    private Participant returnParticipant() {

        User testParticipant = new User();
        LocalDate date = LocalDate.now();
        Participant participant = new Participant(1L
                , "Clara"
                , "de Groot"
                , "clara.degroot@example.com"
                , "Vlissingen"
                , "0612342323"
                , "Beschrijving van participant"
                , date
                , "Unive"
                , testParticipant);

        return participant;
    }

    @Test
    void showParticipantsWithoutCohortReturnsParticipantWhenCohortIdIsNull() {

        Participant participantWithoutCohort = returnParticipant();
        List<Participant> participantList = new ArrayList<>();
        participantList.add(participantWithoutCohort);

        when(participantRepository.findByNullCohort()).thenReturn(participantList);

        assertEquals(participantService.showAllParticipantsWithoutCohort(),participantList);
    }

    @Test
    void showParticipantsWithoutCohortReturnsEmptyListWhenParticipantHasCohort() {

        Participant participantWithoutCohort = returnParticipant();
        Cohort cohort = new Cohort();
        participantWithoutCohort.setCohorts(cohort);

        List<Participant> emptyParticipantList = new ArrayList<>();

        when(participantRepository.findByNullCohort()).thenReturn(emptyParticipantList);

        assertEquals(participantService.showAllParticipantsWithoutCohort(),emptyParticipantList);
    }
}
