package nl.miwnn.cohort19.DeExparts.Overview.service;

import jakarta.persistence.EntityNotFoundException;
import nl.miwnn.cohort19.DeExparts.Overview.model.Cohort;
import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.CohortRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Coen Cuppes
 * Handle bussiness logic for cohorts
 */
@Service
public class CohortService {
    private final CohortRepository cohortRepository;
    private final InstructorService instructorService;

    public CohortService(CohortRepository cohortRepository, InstructorService instructorService) {
        this.cohortRepository = cohortRepository;
        this.instructorService = instructorService;
    }

    public List<Cohort> showAllCohorts(){
        return cohortRepository.findAll();
    }

    public Cohort findById(Long cohortId){
        return cohortRepository.findById(cohortId).orElseThrow(
                () -> new EntityNotFoundException(String.format("No cohort found with id: %d", cohortId))
        );
    }

    @Transactional
    public void saveCohort(Cohort editedCohort) {
        editedCohort.getInstructors().forEach(instructor -> instructor.addCohort(editedCohort));
        editedCohort.getParticipants().forEach(participant -> participant.setCohorts(editedCohort));
        cohortRepository.save(editedCohort);
    }

    @Transactional
    public void deleteCohort(Long id){
        Cohort cohort = findById(id);
        List<Instructor> instructors = cohort.getInstructors();
        List<Participant> participants = cohort.getParticipants();
        for (int i = 0; i < instructors.size(); i++) {
            instructors.get(i).removeCohort(cohort);
        }
        for (int i = 0; i < participants.size(); i++) {
            participants.get(i).setCohorts(null);
        }
        cohortRepository.deleteById(id);
    }
}
