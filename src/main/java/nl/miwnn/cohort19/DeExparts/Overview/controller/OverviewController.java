package nl.miwnn.cohort19.DeExparts.Overview.controller;

import nl.miwnn.cohort19.DeExparts.Overview.repositories.ParticipantRepository;
import nl.miwnn.cohort19.DeExparts.Overview.service.OverviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
@RequestMapping("/overview")
@Controller
public class OverviewController {

    private final OverviewService overviewService;

    private static final Logger log =
            LoggerFactory.getLogger(OverviewController.class);

    public OverviewController(OverviewService overviewService) {
        this.overviewService = overviewService;
    }

    @GetMapping(value = {"/", ""})
    public String showOverview(Model model) {
        log.debug("Overview pagina opgevraagd");
        model.addAttribute("title", "Deelnemer overzicht");
        model.addAttribute("allParticipants", overviewService.showParticipant());
        return "overview";
    }

}
