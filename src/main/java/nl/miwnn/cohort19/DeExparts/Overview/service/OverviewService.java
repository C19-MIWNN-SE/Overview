package nl.miwnn.cohort19.DeExparts.Overview.service;

import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.ParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
@Service
public class OverviewService {
    private final ParticipantRepository participantRepository;

    public OverviewService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public List<Participant> showParticipant(){
        return participantRepository.findAll();
    }
}
