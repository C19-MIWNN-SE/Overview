package nl.miwnn.cohort19.DeExparts.Overview.controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.InstructorRepository;
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
    private final InstructorRepository instructorRepository;

    public InitializeController(ParticipantRepository participantRepository, InstructorRepository instructorRepository) {
        this.participantRepository = participantRepository;
        this.instructorRepository = instructorRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void seed() {
        if (participantRepository.count() == 0) {
            seedParticipants();
        }
        if (instructorRepository.count() == 0) {
            seedInstructors();
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

    private void seedInstructors() {
        try {
            ClassPathResource resource =
                    new ClassPathResource("seedData/instructors.csv");
            Reader reader = new InputStreamReader(
                    resource.getInputStream());
            CsvToBean<Instructor> csvToBean =
                    new CsvToBeanBuilder<Instructor>(reader)
                            .withType(Instructor.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
            instructorRepository.saveAll(csvToBean.parse());
        } catch (IOException e) {
            throw new RuntimeException(
                    "Kon instructor.csv niet inlezen", e);
        }
    }
}
