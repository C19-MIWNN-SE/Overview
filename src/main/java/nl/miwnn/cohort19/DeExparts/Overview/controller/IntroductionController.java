package nl.miwnn.cohort19.DeExparts.Overview.controller;

import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.service.InstructorService;
import nl.miwnn.cohort19.DeExparts.Overview.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
@Controller
@RequestMapping("/detail")
public class IntroductionController {

    private static final Logger log =
            LoggerFactory.getLogger(IntroductionController.class);

    private final ParticipantService participantService;
    private final InstructorService instructorService;

    public IntroductionController(InstructorService instructorService, ParticipantService participantService) {
        this.instructorService = instructorService;
        this.participantService = participantService;
    }

    @GetMapping(value = {"/participant/{id}"})
    public String showParticipantDetail(@PathVariable Long id, Model model) {
        Optional<Participant> participant = participantService.showParticipantDetail(id);
        log.debug("Detail pagina deelnemer opgevraagd");
        model.addAttribute("title", "Detail overzicht");
        model.addAttribute("participant", participant.get());
        model.addAttribute("activePage", "aboutMe");
        return "detail-participant";
    }

    @GetMapping(value = {"/instructor/{id}"})
    public String showInstructorDetail(@PathVariable Long id, Model model) {
        Optional<Instructor> instructor = instructorService.showInstructorDetail(id);
        log.debug("Detail pagina docent opgevraagd");
        model.addAttribute("title", "Detail overzicht");
        model.addAttribute("instructor", instructor.get());
        model.addAttribute("activePage", "aboutMe");
        return "detail-instructor";
    }
}

