package nl.miwnn.cohort19.DeExparts.Overview.controller;

import nl.miwnn.cohort19.DeExparts.Overview.model.Cohort;
import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.service.CohortService;
import nl.miwnn.cohort19.DeExparts.Overview.service.InstructorService;
import nl.miwnn.cohort19.DeExparts.Overview.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.web.ProjectedPayload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
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
    private final InstructorService instructorService;
    private final ParticipantService participantService;

    public CohortController(CohortService cohortService, InstructorService instructorService, ParticipantService participantService) {
        this.cohortService = cohortService;
        this.instructorService = instructorService;
        this.participantService = participantService;
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

    @GetMapping(value = {"/add"})
    public String addCohort(Model model) {
        log.debug("Toevoegingspagina voor cohort opgevraagd");
        model.addAttribute("cohort", new Cohort());
        model.addAttribute("allInstructors", instructorService.showAllInstructors());
        model.addAttribute("allParticipantsWithoutCohort",
                participantService.showAllParticipantsWithoutCohort());
        return "add-cohort";
    }

    @PostMapping(value = {"/save"})
    public String saveCohort(@ModelAttribute Cohort editedCohort,
                             RedirectAttributes redirectAttributes){
        cohortService.saveCohort(editedCohort);
        log.info("Cohort met id {} opgeslagen.", editedCohort.getId());
        redirectAttributes.addAttribute("id", editedCohort.getId());
        return "redirect:/cohort/";
    }
}
