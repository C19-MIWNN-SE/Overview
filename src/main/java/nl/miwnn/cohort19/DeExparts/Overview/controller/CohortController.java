package nl.miwnn.cohort19.DeExparts.Overview.controller;

import jakarta.validation.*;
import nl.miwnn.cohort19.DeExparts.Overview.model.Cohort;
import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.model.User;
import nl.miwnn.cohort19.DeExparts.Overview.service.CohortService;
import nl.miwnn.cohort19.DeExparts.Overview.service.InstructorService;
import nl.miwnn.cohort19.DeExparts.Overview.service.ParticipantService;
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
 * Handles requests related to cohort management, including viewing, adding,
 * editing, deleting, and role-based access to cohort detail pages.
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

    @GetMapping("/all")
    public String showAllCohorts(Model model) {
        log.debug("Overview pagina voor cohorts opgevraagd");
        model.addAttribute("title", "Overzicht alle cohorten");
        model.addAttribute("allCohorts", cohortService.showAllCohorts());
        model.addAttribute("activePage", "cohort");
        return "overview-cohorts";
    }

    @GetMapping("/instructor-overview")
    public String showAllCohortsForInstructor(
            @AuthenticationPrincipal User currentUser,
            Model model) {

        Optional<Instructor> instructor = instructorService.findInstructorByUserId(currentUser.getId());

        if (instructor.isEmpty()) {
            log.warn("Instructor not found for user ID: {}", currentUser.getId());
            model.addAttribute("bericht",
                    "Je hebt geen toegang tot deze pagina. Alleen docenten kunnen dit overzicht zien.");
            return "error/403";
        }

        Long instructorId = instructor.get().getId();
        List<Cohort> allCohortsForUserInstructor = cohortService.showAllCohortsForInstructor(instructorId);

        log.debug("Overview pagina voor cohorts van docent opgevraagd");
        log.debug("Instructor ID: {}", instructorId);

        model.addAttribute("title", "Overzicht eigen cohorten");
        model.addAttribute("allCohorts", allCohortsForUserInstructor);
        model.addAttribute("activePage", "cohort");

        return "overview-cohorts";
    }

    @GetMapping("/{cohortId}")
    public String showCohortDetails(
            @PathVariable Long cohortId,
            Model model,
            @AuthenticationPrincipal User currentUser) {
        log.debug("Overview pagina voor specifieke cohort opgevraagd");

        if (currentUser.isParticipant()) {
            Long userCohortId = currentUser.getParticipant().getCohorts().getId();
            if (!userCohortId.equals(cohortId)){
                model.addAttribute("bericht",
                        "Je hebt geen toegang tot deze cohort.");
                return "error/403";
            }
        }

        model.addAttribute("title", "Overzicht cohort");
        model.addAttribute("cohort", cohortService.findById(cohortId));
        model.addAttribute("activePage", "cohort");

        return "detail-cohort";
    }

    @PostMapping("/delete/{id}")
    public String deleteCohort(@PathVariable Long id){
        log.info("Verwijderaanvraag voor cohort met id: {} aangevraagd",id);
        cohortService.findById(id);

        cohortService.deleteCohort(id);
        log.info("Cohort met id {} succesvol verwijderd.",id);
        return "redirect:/cohort/all";
    }

    @GetMapping("/add")
    public String addCohort(Model model) {
        log.debug("Toevoegingspagina voor cohort opgevraagd");
        model.addAttribute("cohort", new Cohort());
        model.addAttribute("allInstructors", instructorService.showAllInstructors());
        model.addAttribute("allParticipantsWithoutCohort",
                participantService.showAllParticipantsWithoutCohort());
        return "add-cohort";
    }

    @GetMapping("/edit/{id}")
    public String editCohort(@PathVariable Long id, Model model){
        Cohort cohort = cohortService.findById(id);
        log.debug("Bewerkingspagina voor cohort met id {} opgevraagd", id);
        model.addAttribute("cohort", cohort);
        model.addAttribute("allInstructors", instructorService.showAllInstructors());
        model.addAttribute("allParticipantsInCohort", cohortService.showParticipantsInCohort(id));
        model.addAttribute("allParticipantsWithoutCohort",
                participantService.showAllParticipantsWithoutCohort());
        return "edit-cohort";
    }

    @PostMapping("/save")
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
        return "redirect:/cohort/{id}";
    }

    @PostMapping("/{id}/search")
    public String searchParticipant(@PathVariable Long id, @RequestParam String name, Model model) {
        model.addAttribute("cohort", cohortService.findById(id));
        model.addAttribute("searchResults", participantService.findByName(name));
        return "detail-cohort";
    }
}
