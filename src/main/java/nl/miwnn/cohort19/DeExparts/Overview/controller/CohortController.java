package nl.miwnn.cohort19.DeExparts.Overview.controller;

import jakarta.validation.*;
import nl.miwnn.cohort19.DeExparts.Overview.model.Cohort;
import nl.miwnn.cohort19.DeExparts.Overview.model.User;
import nl.miwnn.cohort19.DeExparts.Overview.service.CohortService;
import nl.miwnn.cohort19.DeExparts.Overview.service.InstructorService;
import nl.miwnn.cohort19.DeExparts.Overview.service.ParticipantService;
import nl.miwnn.cohort19.DeExparts.Overview.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
@RequestMapping("/cohort")
@Controller
public class CohortController {

    private static final Logger log =
            LoggerFactory.getLogger(CohortController.class);

    private final CohortService cohortService;
    private final InstructorService instructorService;
    private final ParticipantService participantService;

    public CohortController(CohortService cohortService,
                            InstructorService instructorService,
                            ParticipantService participantService) {
        this.cohortService = cohortService;
        this.instructorService = instructorService;
        this.participantService = participantService;
    }

    @GetMapping(value = {"/", ""})
    public String showAllCohorts(Model model) {
        log.debug("Overview pagina voor cohorts opgevraagd");
        model.addAttribute("title", "Overzicht cohorts");
        model.addAttribute("allCohorts", cohortService.showAllCohorts());
        return "overview-cohorts";
    }

    // TODO improve method
    @GetMapping("/instructor-overview")
    public String showAllCohortsForUserInstructor(
            @AuthenticationPrincipal User currentUser,
            Model model) {

        // TODO clean up and simplify code
        Long instructorId = instructorService.findInstructorByUserId(currentUser.getId()).get().getId();
        List<Cohort> allCohortsForUserInstructor = cohortService.showAllCohortsForInstructor(instructorId);

        log.debug("Overview pagina voor cohorts van docent opgevraagd");
        log.debug("Instructor ID: {}", instructorService.findInstructorByUserId(currentUser.getId()));
        model.addAttribute("title", "Overzicht cohorts van docent");
        model.addAttribute("allCohorts", allCohortsForUserInstructor);
        model.addAttribute("activePage", "cohort");
        return "overview-cohorts";
    }

    @GetMapping(value ="/{cohortId}")
    public String showCohortDetails(
            @PathVariable Long cohortId,
            Model model,
            @AuthenticationPrincipal User currentUser) {
        log.debug("Overview pagina voor specifieke cohort opgevraagd");

        if (currentUser.isParticipant()) {
            Long userCohortId = currentUser.getParticipant().getCohorts().getId();
            if (!userCohortId.equals(cohortId)){
                return "redirect:/JeHebtAlleenToegangTotJeEigenKlas";
            }
            model.addAttribute("userType","participant");
        } else if (currentUser.isInstructor()) {
            model.addAttribute("userType", "instructor");
        }

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

    @GetMapping(value = {"/edit/{id}"})
    public String editCohort(@PathVariable Long id, Model model){
        Optional<Cohort> cohort = Optional.ofNullable(cohortService.findById(id));
        log.debug("Bewerkingspagina voor cohort met id {} opgevraagd", id);
        model.addAttribute("cohort", cohort.get());
        model.addAttribute("allInstructors", instructorService.showAllInstructors());
        model.addAttribute("allParticipantsInCohort", cohortService.showParticipantsInCohort(id));
        model.addAttribute("allParticipantsWithoutCohort",
                participantService.showAllParticipantsWithoutCohort());
        return "edit-cohort";
    }

    @PostMapping(value = {"/save"})
    public String saveCohort(@Valid @ModelAttribute Cohort editedCohort,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model){
        if (bindingResult.hasErrors()){
            log.warn("Validatiefouten bij het opslaan: {}", bindingResult.getErrorCount());
            model.addAttribute("cohort", editedCohort);
            model.addAttribute("allInstructors", instructorService.showAllInstructors());
            model.addAttribute("allParticipantsWithoutCohort",
                    participantService.showAllParticipantsWithoutCohort());
            return "add-cohort";
        }
        cohortService.saveCohort(editedCohort);
        log.info("Cohort met id {} opgeslagen.", editedCohort.getId());
        redirectAttributes.addAttribute("id", editedCohort.getId());
        return "redirect:/cohort/";
    }
}
