package nl.miwnn.cohort19.DeExparts.Overview.repositories;

import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Author: Anouk de Vos
 * Repository for Instructor entities, with a method to look up
 * an instructor by their linked user account ID.
 */
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Optional<Instructor> findInstructorByUser_Id(Long userId);

}
