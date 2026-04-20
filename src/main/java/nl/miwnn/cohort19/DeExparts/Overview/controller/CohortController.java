package nl.miwnn.cohort19.DeExparts.Overview.controller;

import nl.miwnn.cohort19.DeExparts.Overview.model.Cohort;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.service.CohortService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
@RequestMapping("/cohort")
@Controller
public class CohortController {

    private final CohortService cohortService;

    private static final Logger log =
            LoggerFactory.getLogger(CohortController.class);

    public CohortController(CohortService cohortService) {
        this.cohortService = cohortService;
    }

    @GetMapping(value = {"/", ""})
    public String showAllCohorts(Model model) {
        log.debug("Overview pagina voor cohorts opgevraagd");
        model.addAttribute("title", "Overzicht cohorts");
        model.addAttribute("allCohorts", cohortService.showAllCohorts());
        model.addAttribute("activePage", "cohort");
        return "overview-cohorts";
    }

    @GetMapping(value ="/{cohortId}")
    public String showCohortDetails(@PathVariable Long cohortId, Model model) {
        log.debug("Overview pagina voor specifieke cohort opgevraagd");

        model.addAttribute("title", "Overzicht cohort");
        model.addAttribute("cohort", cohortService.findById(cohortId));
        model.addAttribute("activePage", "cohort");
        return "detail-cohort";
    }

    @PostMapping(value = {"/delete/{id}"})
    public String deleteParticipant(@PathVariable Long id, RedirectAttributes redirectAttributes){
        Optional<Cohort> cohort = Optional.ofNullable(cohortService.findById(id));
        log.info("Verwijderaanvraag voor cohort met id: {} aangevraagd",id);
        if (cohort.isEmpty()){
            log.warn("participant met id {} niet gevonden",id);
            redirectAttributes.addAttribute("id", id);
            return "redirect:/cohort/{id}";
        } else {
            cohortService.deleteCohort(id);
            log.info("Cohort met id {} succesvol verwijderd.",id);
            return "redirect:/cohort/";
        }
    }
}
