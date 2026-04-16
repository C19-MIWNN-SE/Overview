package nl.miwnn.cohort19.DeExparts.Overview.service;

import jakarta.transaction.Transactional;
import nl.miwnn.cohort19.DeExparts.Overview.model.Cohort;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.CohortRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public Optional<Cohort> showCohort(Long cohortId){
        return cohortRepository.findById(cohortId);
    }
}
