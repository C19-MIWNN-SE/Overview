package nl.miwnn.cohort19.DeExparts.Overview.repositories;

import nl.miwnn.cohort19.DeExparts.Overview.model.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wat doe ik?
 */
public interface CohortRepository extends JpaRepository<Cohort, Long> {
    @Query("SELECT c FROM Instructor i JOIN i.cohorts c WHERE i.id = :instructorId")
    List<Cohort> findCohortsByInstructorId(@Param("instructorId") Long instructorId);
}
