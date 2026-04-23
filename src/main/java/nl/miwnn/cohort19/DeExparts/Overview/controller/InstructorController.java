package nl.miwnn.cohort19.DeExparts.Overview.controller;

import jakarta.validation.Valid;
import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.service.CohortService;
import nl.miwnn.cohort19.DeExparts.Overview.service.InstructorService;
import nl.miwnn.cohort19.DeExparts.Overview.service.ParticipantService;
import nl.miwnn.cohort19.DeExparts.Overview.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * @author wat doe ik?
 */
@Controller
@RequestMapping("/detail/instructor")
public class InstructorController {
    private static final Logger log =
            LoggerFactory.getLogger(InstructorController.class);

    private final InstructorService instructorService;
    private final CohortService cohortService;

    public InstructorController(InstructorService instructorService, CohortService cohortService) {
        this.instructorService = instructorService;
        this.cohortService = cohortService;
    }

    @GetMapping(value = {"/{id}"})
    public String showInstructorDetail(@PathVariable Long id, Model model) {
        Optional<Instructor> instructor = instructorService.showInstructorDetail(id);
        log.debug("Detail pagina docent opgevraagd");
        model.addAttribute("title", "Detail overzicht");
        model.addAttribute("instructor", instructor.get());
        return "detail-instructor";
    }

    @PostMapping(value = {"/delete/{id}"})
    public String deleteInstructor(@PathVariable Long id){
        Optional<Instructor> instructor = instructorService.showInstructorDetail(id);
        log.info("Verwijderaanvraag voor docent met id: {} aangevraagd",id);
        if (instructor.isEmpty()){
            log.warn("docent met id {} niet gevonden",id);
            return "redirect:/instructor/{id}";
        } else {
            instructorService.deleteInstructor(id);
            log.info("Instructor met id {} succesvol verwijderd.",id);
            return "redirect:/cohort/";
        }
    }

    @GetMapping(value = {"/edit/{id}"})
    public String editInstructor(@PathVariable Long id, Model model){
        Optional<Instructor> instructor = instructorService.showInstructorDetail(id);
        log.debug("Bewerkingspagina voor instructor met id {} opgevraagd", id);
        model.addAttribute("instructor", instructor.get());
        model.addAttribute("allCohorts", cohortService.showAllCohorts());
        return "edit-instructor";
    }

    @PostMapping(value = {"/save"})
    public String saveInstructor(@Valid @ModelAttribute Instructor editedInstructor,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model){
        if (bindingResult.hasErrors()){
            log.warn("Validatiefouten bij het opslaan: {}", bindingResult.getErrorCount());
            model.addAttribute("participant", editedInstructor);
            model.addAttribute("allCohorts", cohortService.showAllCohorts());
            return "edit-instructor";
        }
        log.info("Docent met id {} opgeslagen.", editedInstructor.getId());
        instructorService.saveInstructor(editedInstructor);
        redirectAttributes.addAttribute("id", editedInstructor.getId());
        return "redirect:/detail/instructor/{id}";
    }

    @GetMapping(value = {"/add"})
    public String addInstructor(Model model){
        log.debug("Toevoegingspagina voor instructor opgevraagd");
        model.addAttribute("instructor", new Instructor());
        model.addAttribute("allCohorts", cohortService.showAllCohorts());
        return "add-instructor";
    }
}
