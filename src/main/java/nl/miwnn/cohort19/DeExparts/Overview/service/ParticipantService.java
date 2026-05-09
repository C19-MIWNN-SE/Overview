package nl.miwnn.cohort19.DeExparts.Overview.service;

import jakarta.persistence.EntityNotFoundException;
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
 * Service class for participant-related business logic, providing methods
 * to retrieve, save, and delete participants, including lookup by user ID
 * and filtering participants without an assigned cohort.
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
        long time = System.currentTimeMillis();
        List<Participant> participants = participantRepository.findAll();
        System.out.println("showAllParticipants: " + (System.currentTimeMillis() - time));
        return participants;
    }

    @Transactional
    public List<Participant> showAllParticipantsWithoutCohort() {
        long time = System.currentTimeMillis();
//        List<Participant> participants = showAllParticipants();
//        System.out.println("showAllParticipants in Wihtout Cohort: " + (System.currentTimeMillis() - time));
//        List<Participant> participantsWithoutCohort = new ArrayList<>();
//        for (int i = 0; i < participants.size(); i++) {
//            Participant participant = participants.get(i);
//            if (participant.getCohorts() == null) {
//                participantsWithoutCohort.add(participant);
//            }
//        }
        List<Participant> participants = participantRepository.findByNullCohort();
        System.out.println("showAllParticipantsWithoutCohort: " + (System.currentTimeMillis() - time));
        return participants;
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

    public Participant findById(long participantId) {
        return participantRepository.findById(participantId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Deelnemer met id %d niet gevonden", participantId)));
    }

    public void deleteParticipant(Long id){
        participantRepository.deleteById(id);
    }

    @Transactional
    public void saveParticipant(Participant editedParticipant){
        participantRepository.save(editedParticipant);
    }
}
