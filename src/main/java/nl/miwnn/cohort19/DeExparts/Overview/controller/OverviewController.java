package nl.miwnn.cohort19.DeExparts.Overview.controller;

import nl.miwnn.cohort19.DeExparts.Overview.service.CohortService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
@RequestMapping("/overview")
@Controller
public class OverviewController {

    private final CohortService cohortService;

    private static final Logger log =
            LoggerFactory.getLogger(OverviewController.class);

    public OverviewController(CohortService cohortService) {
        this.cohortService = cohortService;
    }

    @GetMapping(value = {"/", ""})
    public String showAllCohorts(Model model) {
        log.debug("Overview pagina voor cohorts opgevraagd");
        model.addAttribute("title", "Overzicht cohorts");
        model.addAttribute("allCohorts", cohortService.showAllCohorts());
        return "overview";
    }

    @GetMapping(value = {"/cohort/{cohortId}", ""})
    public String showCohortDetails(@PathVariable Long cohortId, Model model) {
        log.debug("Overview pagina voor specifieke cohort opgevraagd");
        model.addAttribute("title", "Overzicht cohort");
        model.addAttribute("cohort", cohortService.showCohort(cohortId));
        return "overview-cohort";
    }
}
