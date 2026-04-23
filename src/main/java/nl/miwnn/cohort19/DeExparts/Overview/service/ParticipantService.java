package nl.miwnn.cohort19.DeExparts.Overview.service;

import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.CohortRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.ParticipantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Coen Cuppes
 * !! TODO include description !!
 */
@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final CohortRepository cohortRepository;

    public ParticipantService(ParticipantRepository participantRepository, CohortRepository cohortRepository) {
        this.participantRepository = participantRepository;
        this.cohortRepository = cohortRepository;
    }

    public List<Participant> showAllParticipants(){
        return participantRepository.findAll();
    }

    public List<Participant> showAllParticipantsWithoutCohort() {
        List<Participant> participants = showAllParticipants();
        List<Participant> participantsWithoutCohort = new ArrayList<>();
        for (int i = 0; i < participants.size(); i++) {
            Participant participant = participants.get(i);
            if (participant.getCohorts() == null) {
                participantsWithoutCohort.add(participant);
            }
        }
        return participantsWithoutCohort;
    }

    public Optional<Participant> showParticipantDetail(Long id){
        return participantRepository.findById(id);
    }

    public Optional<Participant> findParticipantByUserId(Long userId){
        return participantRepository.findParticipantByUser_Id(userId);
    }

    public List<Participant> showParticipantsInSpecificCohort(long cohortID) {
        return participantRepository.findByCohorts_Id(cohortID);
    }

    public void deleteParticipant(Long id){
        participantRepository.deleteById(id);
    }

    @Transactional
    public void saveParticipant(Participant editedParticipant){
        participantRepository.save(editedParticipant);
    }
}
