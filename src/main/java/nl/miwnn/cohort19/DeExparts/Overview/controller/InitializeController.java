package nl.miwnn.cohort19.DeExparts.Overview.controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.ParticipantRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */

@Controller
public class InitializeController {

    private final ParticipantRepository participantRepository;

    public InitializeController(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void seed() {
        if (participantRepository.count() == 0) {
            seedParticipants();
        }
    }

    private void seedParticipants() {
        try {
            ClassPathResource resource =
                    new ClassPathResource("seedData/participants.csv");
            Reader reader = new InputStreamReader(
                    resource.getInputStream());
            CsvToBean<Participant> csvToBean =
                    new CsvToBeanBuilder<Participant>(reader)
                            .withType(Participant.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
            participantRepository.saveAll(csvToBean.parse());
        } catch (IOException e) {
            throw new RuntimeException(
                    "Kon participants.csv niet inlezen", e);
        }
    }
}
