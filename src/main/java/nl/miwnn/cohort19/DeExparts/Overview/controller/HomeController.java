package nl.miwnn.cohort19.DeExparts.Overview.controller;

import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.service.HomeService;
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
 * @author wat doe ik?
 */
@Controller
@RequestMapping("/home")
public class HomeController {
    private static final Logger log =
            LoggerFactory.getLogger(OverviewController.class);
    private final HomeService homeService;
    private final IntroductionService introductionService;

    public HomeController(HomeService homeService, IntroductionService introductionService) {
        this.homeService = homeService;
        this.introductionService = introductionService;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        Optional<Participant> participant = introductionService.showParticipantDetail(1L);
        log.debug("Home pagina opgevraagd");
        model.addAttribute("title", "Homepagina");
        model.addAttribute("participant", participant.get());
        return "home";
    }

}
