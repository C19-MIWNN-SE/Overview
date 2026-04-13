package nl.miwnn.cohort19.DeExparts.Overview.repositories;

import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
