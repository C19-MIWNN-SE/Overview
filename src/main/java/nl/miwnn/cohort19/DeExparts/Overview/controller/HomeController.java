package nl.miwnn.cohort19.DeExparts.Overview.controller;

import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.service.InstructorService;
import nl.miwnn.cohort19.DeExparts.Overview.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * @author wat doe ik?
 */
@Controller
public class HomeController {
    private static final Logger log =
            LoggerFactory.getLogger(HomeController.class);

    private final ParticipantService participantService;
    private final InstructorService instructorService;

    public HomeController(ParticipantService participantService, InstructorService instructorService) {
        this.participantService = participantService;
        this.instructorService = instructorService;
    }

    @GetMapping({"/home", "/home/","/"})
    public String showHomePage(Model model) {
        Optional<Participant> participant = participantService.showParticipantDetail(1L);
        log.debug("Home pagina opgevraagd");
        model.addAttribute("title", "Homepagina");
        model.addAttribute("participant", participant.get());
        return "home";
    }

}
