package nl.miwnn.cohort19.DeExparts.Overview.service;

import jakarta.persistence.EntityNotFoundException;
import nl.miwnn.cohort19.DeExparts.Overview.model.Cohort;
import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.CohortRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Coen Cuppes
 * Handle bussiness logic for cohorts
 */
@Service
public class CohortService {
    private final CohortRepository cohortRepository;
    private final InstructorService instructorService;
    private final ParticipantService participantService;

    public CohortService(CohortRepository cohortRepository,
                         InstructorService instructorService,
                         ParticipantService participantService) {
        this.cohortRepository = cohortRepository;
        this.instructorService = instructorService;
        this.participantService = participantService;
    }

    public List<Participant> showParticipantsInCohort(Long id) {
        long time = System.currentTimeMillis();
        List<Participant> participants = findById(id).getParticipants();
        System.out.println("showParticipantsInCohort: " + (System.currentTimeMillis() - time));
        return participants;
    }

    public List<Instructor> showInstructorsInCohort(Long id) {return findById(id).getInstructors();}

    public List<Participant> showParticipantInCohortAndWithout(Long id){
        List<Participant> participants = participantService.showAllParticipantsWithoutCohort();
        return showParticipantsInCohort(id);
    }

    public List<Cohort> showAllCohorts(){
        return cohortRepository.findAll();
    }

    public List<Cohort> showAllCohortsForInstructor(Long instructorId){
        return cohortRepository.findCohortsByInstructorId(instructorId);
    }

    public Cohort findById(Long cohortId){
        return cohortRepository.findById(cohortId).orElseThrow(
                () -> new EntityNotFoundException(String.format("No cohort found with id: %d", cohortId))
        );
    }

    @Transactional
    public void saveCohort(Cohort editedCohort) {
        Cohort savedCohort = cohortRepository.save(editedCohort);

        savedCohort.getInstructors().forEach(instructor -> {
            if (!instructor.getCohorts().contains(savedCohort)) {
                instructor.addCohort(savedCohort);
                instructorService.saveInstructor(instructor);
            }
        });

        savedCohort.getParticipants().forEach(participant -> {
            participant.setCohorts(savedCohort);
            participantService.saveParticipant(participant);
        });
    }

    @Transactional
    public void deleteCohort(Long id){
        Cohort cohort = findById(id);

        List<Instructor> instructorsCopy = new ArrayList<>(cohort.getInstructors());

        for (Instructor instructor : instructorsCopy) {
            instructor.removeCohort(cohort);
            instructorService.saveInstructor(instructor);
        }

        List<Participant> participantsCopy = new ArrayList<>(cohort.getParticipants());
        for (Participant participant : participantsCopy) {
            participant.setCohorts(null);
            participantService.saveParticipant(participant);
        }

        cohortRepository.deleteById(id);
    }
}
