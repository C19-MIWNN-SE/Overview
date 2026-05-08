package nl.miwnn.cohort19.DeExparts.Overview.service;

import jakarta.persistence.EntityNotFoundException;
import nl.miwnn.cohort19.DeExparts.Overview.model.Cohort;
import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.CohortRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.InstructorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Coen Cuppes
 * Service class for instructor-related business logic, providing methods
 * to retrieve, save, and delete instructors, including lookup by user ID.
 */
@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final CohortRepository cohortRepository;

    public InstructorService(InstructorRepository instructorRepository, CohortRepository cohortRepository) {
        this.instructorRepository = instructorRepository;
        this.cohortRepository = cohortRepository;
    }

    public List<Instructor> showAllInstructors() {
        long time = System.currentTimeMillis();
        List<Instructor> instructors = instructorRepository.findAll();
        System.out.println("showAllInstructors: " + (System.currentTimeMillis() - time));
        return instructors;}


    public Optional<Instructor> showInstructorDetail(Long id) {
        return instructorRepository.findById(id);
    }

    public Optional<Instructor> findInstructorByUserId(Long userId){
        return instructorRepository.findInstructorByUser_Id(userId);
    }

    public Instructor findById(long instructorId) {
        return instructorRepository.findById(instructorId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Docent met id %d niet gevonden", instructorId)));
    }

    public void deleteInstructor(Long id){
        instructorRepository.deleteById(id);
    }

    @Transactional
    public void saveInstructor(Instructor editedInstructor){
        instructorRepository.save(editedInstructor);
    }
}
