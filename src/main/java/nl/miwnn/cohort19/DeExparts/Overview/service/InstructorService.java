package nl.miwnn.cohort19.DeExparts.Overview.service;

import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.InstructorRepository;
import org.springframework.stereotype.Service;

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

    public Optional<Instructor> showInstructorDetail(Long id) {
        return instructorRepository.findById(id);
    }
}
