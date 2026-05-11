package nl.miwnn.cohort19.DeExparts.Overview.repositories;

import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Author: Anouk de Vos
 * Repository for Participant entities, with methods to find a participant
 * by user ID, retrieve participants without an assigned cohort,
 * and retrieve participants belonging to a specific cohort.
 */
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findParticipantByUser_Id(Long userId);

    @Query("select p from Participant p where p.cohorts.id is null")
    List<Participant> findByNullCohort();

    List<Participant> findByCohorts_Id(Long cohortsId);

    List<Participant> findByFirstNameContainingIgnoreCase(String name);
}
