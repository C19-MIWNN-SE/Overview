package nl.miwnn.cohort19.DeExparts.Overview.controller;

import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.service.IntroductionService;
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
            LoggerFactory.getLogger(OverviewController.class);
    private final IntroductionService introductionService;

    public IntroductionController(IntroductionService introductionService) {
        this.introductionService = introductionService;
    }

    @GetMapping(value = {"/participant/{id}"})
    public String showParticipantDetail(@PathVariable Long id, Model model) {
        Optional<Participant> participant = introductionService.showParticipantDetail(id);
        log.debug("Detail pagina opgevraagd");
        model.addAttribute("title", "Detail overzicht");
        model.addAttribute("participant", participant.get());
        return "detail-participant";
    }

    @GetMapping(value = {"/instructor/{id}"})
    public String showInstructorDetail(@PathVariable Long id, Model model) {
        Optional<Instructor> instructor = introductionService.showInstructorDetail(id);
        log.debug("Detail pagina opgevraagd");
        model.addAttribute("title", "Detail overzicht");
        model.addAttribute("instructor", instructor.get());
        return "detail-instructor";
    }
}

