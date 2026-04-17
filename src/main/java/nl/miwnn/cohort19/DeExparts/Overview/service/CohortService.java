package nl.miwnn.cohort19.DeExparts.Overview.service;

import jakarta.persistence.EntityNotFoundException;
import nl.miwnn.cohort19.DeExparts.Overview.model.Cohort;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.CohortRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Coen Cuppes
 * Handle bussiness logic for cohorts
 */
@Service
public class CohortService {
    private final CohortRepository cohortRepository;

    public CohortService(CohortRepository cohortRepository) {
        this.cohortRepository = cohortRepository;
    }

    public List<Cohort> showAllCohorts(){
        return cohortRepository.findAll();
    }

    public Cohort findById(Long cohortId){
        return cohortRepository.findById(cohortId).orElseThrow(
                () -> new EntityNotFoundException(String.format("No cohort found with id: %d", cohortId))
        );
    }
}
