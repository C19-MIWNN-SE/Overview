package nl.miwnn.cohort19.DeExparts.Overview.service;

import jakarta.servlet.http.Part;
import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.InstructorRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.ParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
@Service
public class IntroductionService {

  private final ParticipantRepository participantRepository;
  private final InstructorRepository instructorRepository;

  public IntroductionService(ParticipantRepository participantRepository, InstructorRepository instructorRepository){
      this.participantRepository = participantRepository;
      this.instructorRepository = instructorRepository;
  }

  public Optional<Participant> showParticipantDetail(Long id){
      Optional<Participant> participant = participantRepository.findById(id);
      return participant;
  }

    public Optional<Instructor> showInstructorDetail(Long id) {
        Optional<Instructor> instructor = instructorRepository.findById(id);
        return instructor;
    }


}
