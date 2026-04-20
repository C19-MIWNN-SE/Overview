package nl.miwnn.cohort19.DeExparts.Overview.controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import nl.miwnn.cohort19.DeExparts.Overview.model.Cohort;
import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.model.User;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.CohortRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.InstructorRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.ParticipantRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */

@Controller
public class InitializeController {

    private final ParticipantRepository participantRepository;
    private final InstructorRepository instructorRepository;
    private final CohortRepository cohortRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final Logger log = LoggerFactory.getLogger(InitializeController.class);

    public InitializeController(
            ParticipantRepository participantRepository,
            InstructorRepository instructorRepository,
            CohortRepository cohortRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.participantRepository = participantRepository;
        this.instructorRepository = instructorRepository;
        this.cohortRepository = cohortRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void seed() {
        if (userRepository.count() == 0) {
            String password = "admin";

            log.info("========================================================================================");
            log.info("Generated password for 'beheerder' : {}", password);
            log.info("========================================================================================");

            User admin = new User(
                    "admin",
                    passwordEncoder.encode(password),
                    true);
            userRepository.save(admin);
        }

        if(cohortRepository.count() == 0){
            seedCohorts();
        }

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

            List<Participant> participants = csvToBean.parse();
            List<Cohort> cohorts = cohortRepository.findAll();

            for (int i = 0; i < participants.size(); i++) {
                Participant participant = participants.get(i);
                participant.setCohorts(cohorts.get(i % cohorts.size()));
                participantRepository.save(participant);
            }

            for (int i = 0; i < participants.size(); i++) {
                Participant participant = participants.get(i);

                String username = participant.getFullName();
                String password = "pw" + i;
                User user = new User(username, passwordEncoder.encode(password), false);
                userRepository.save(user);
                participant.setUser(user);

                participantRepository.save(participant);

                log.info("========================================================================================");
                log.info("Generated password for '{}' : {}", username, password);
                log.info("========================================================================================");
            }

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

            List<Instructor> instructors = csvToBean.parse();
            List<Cohort> cohorts = cohortRepository.findAll();

            for (int i = 0; i < instructors.size(); i++) {
                Instructor instructor = instructors.get(i);
                instructor.getCohorts().add(cohorts.get(i % cohorts.size()));

                String username = instructor.getFullName();
                String password = "pw" + i;
                User user = new User(username, passwordEncoder.encode(password), false);
                userRepository.save(user);
                instructor.setUser(user);

                instructorRepository.save(instructor);

                log.info("========================================================================================");
                log.info("Generated password for '{}' : {}", username, password);
                log.info("========================================================================================");
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Kon instructor.csv niet inlezen", e);
        }
    }

    private void seedCohorts() {
        try {
            ClassPathResource resource =
                    new ClassPathResource("seedData/cohorts.csv");
            Reader reader = new InputStreamReader(
                    resource.getInputStream());
            CsvToBean<Cohort> csvToBean =
                    new CsvToBeanBuilder<Cohort>(reader)
                            .withType(Cohort.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
            cohortRepository.saveAll(csvToBean.parse());
        } catch (IOException e) {
            throw new RuntimeException(
                    "Kon cohort.csv niet inlezen", e);
        }
    }

}
