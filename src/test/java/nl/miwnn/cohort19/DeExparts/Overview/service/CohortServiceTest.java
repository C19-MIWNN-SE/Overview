package nl.miwnn.cohort19.DeExparts.Overview.service;

import jakarta.persistence.EntityNotFoundException;
import nl.miwnn.cohort19.DeExparts.Overview.model.Cohort;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.CohortRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Coen Cuppes
 */
@ExtendWith(MockitoExtension.class)
public class CohortServiceTest {
    @Mock
    CohortRepository cohortRepository;

    @InjectMocks
    CohortService cohortService;

    private Cohort createTestCohort() {
        return new Cohort("TestCohort", "Testing",
                LocalDate.now(), LocalDate.now().plusMonths(6));
    }

    @Test
    @DisplayName("findById returns cohort when cohort exists")
    void findByIdReturnsCohortwhenCohortExists() {
        Cohort cohort = createTestCohort();
        when(cohortRepository.findById(1L)).thenReturn(Optional.of(cohort));

        Cohort result = cohortService.findById(1L);

        assertEquals(cohort, result);
    }

    @Test
    @DisplayName("findById throws EntityNotFoundException when cohort does not exist")
    void findByIdThrowsEntityNotFoundExceptionwhenCohortDoesNotExist() {
        when(cohortRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cohortService.findById(99L));
    }

    @Test
    @DisplayName("showAllCohorts returns all cohorts")
    void showAllCohortsReturnsAllCohorts() {
        Cohort cohort1 = createTestCohort();
        Cohort cohort2 = createTestCohort();
        List<Cohort> cohorts = List.of(cohort1, cohort2);

        when(cohortRepository.findAll()).thenReturn(cohorts);

        List<Cohort> result = cohortService.showAllCohorts();

        assertEquals(2, result.size());
        assertEquals(cohort1, result.get(0));
        assertEquals(cohort2, result.get(1));
    }
}
