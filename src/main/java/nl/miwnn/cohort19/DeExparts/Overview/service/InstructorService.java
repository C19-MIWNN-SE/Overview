package nl.miwnn.cohort19.DeExparts.Overview.service;

import jakarta.persistence.EntityNotFoundException;
import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.InstructorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Coen Cuppes
 * !! TODO include description !!
 */
@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public List<Instructor> showAllInstructors() {return instructorRepository.findAll();}

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
