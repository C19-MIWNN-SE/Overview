package nl.miwnn.cohort19.DeExparts.Overview.controller;

import jakarta.validation.Valid;
import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.CohortRepository;
import nl.miwnn.cohort19.DeExparts.Overview.service.CohortService;
import nl.miwnn.cohort19.DeExparts.Overview.service.InstructorService;
import nl.miwnn.cohort19.DeExparts.Overview.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
@Controller
@RequestMapping("/detail")
public class IntroductionController {

    private static final Logger log =
            LoggerFactory.getLogger(IntroductionController.class);

    private final ParticipantService participantService;
    private final InstructorService instructorService;
    private final CohortService cohortService;

    public IntroductionController(
            InstructorService instructorService,
            ParticipantService participantService,
            CohortService cohortService, CohortRepository cohortRepository) {
        this.instructorService = instructorService;
        this.participantService = participantService;
        this.cohortService = cohortService;
    }

    @GetMapping(value = {"/participant/{id}"})
    public String showParticipantDetail(@PathVariable Long id, Model model) {
        Optional<Participant> participant = participantService.showParticipantDetail(id);
        log.debug("Detail pagina deelnemer opgevraagd");
        model.addAttribute("title", "Detail overzicht");
        model.addAttribute("participant", participant.get());
        model.addAttribute("activePage", "aboutMe");
        return "detail-participant";
    }

    @GetMapping(value = {"/instructor/{id}"})
    public String showInstructorDetail(@PathVariable Long id, Model model) {
        Optional<Instructor> instructor = instructorService.showInstructorDetail(id);
        log.debug("Detail pagina docent opgevraagd");
        model.addAttribute("title", "Detail overzicht");
        model.addAttribute("instructor", instructor.get());
        model.addAttribute("activePage", "aboutMe");
        return "detail-instructor";
    }

    @PostMapping(value = {"/participant/delete/{id}"})
    public String deleteParticipant(@PathVariable Long id){
        Optional<Participant> participant = participantService.showParticipantDetail(id);
        log.info("Verwijderaanvraag voor deelnemer met id: {} aangevraagd",id);
        if (participant.isEmpty()){
            log.warn("participant met id {} niet gevonden",id);
            return "redirect:/participant/{id}";
        } else {
            participantService.deleteParticipant(id);
            log.info("Participant met id {} succesvol verwijderd.",id);
            return "redirect:/cohort/";
        }
    }

    @PostMapping(value = {"/instructor/delete/{id}"})
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

    @GetMapping(value = {"/participant/edit/{id}"})
    public String editParticipant(@PathVariable Long id, Model model){
        Optional<Participant> participant = participantService.showParticipantDetail(id);
        log.debug("Bewerkingspagina voor participant met id {} opgevraagd", id);
        model.addAttribute("participant", participant.get());
        model.addAttribute("allCohorts", cohortService.showAllCohorts());
        return "edit-participant";
    }

    @PostMapping(value = {"/participant/save"})
    public String saveParticipant(@Valid @ModelAttribute Participant editedParticipant,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  Model model){
        if (bindingResult.hasErrors()){
            log.warn("Validatiefouten bij het opslaan: {}", bindingResult.getErrorCount());
            model.addAttribute("participant", editedParticipant);
            model.addAttribute("allCohorts", cohortService.showAllCohorts());
            return "edit-participant";
        }
        participantService.saveParticipant(editedParticipant);
        redirectAttributes.addAttribute("id", editedParticipant.getId());
        log.info("Deelnemer met id {} opgeslagen.", editedParticipant.getId());
        return "redirect:/detail/participant/{id}";
    }

    @GetMapping(value = {"/instructor/edit/{id}"})
    public String editInstructor(@PathVariable Long id, Model model){
        Optional<Instructor> instructor = instructorService.showInstructorDetail(id);
        log.debug("Bewerkingspagina voor instructor met id {} opgevraagd", id);
        model.addAttribute("instructor", instructor.get());
        model.addAttribute("allCohorts", cohortService.showAllCohorts());
        return "edit-instructor";
    }

    @PostMapping(value = {"/instructor/save"})
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

    @GetMapping(value = {"/participant/add"})
    public String addParticipant(Model model) {
        log.debug("Toevoegingspagina voor participant met opgevraagd");
        model.addAttribute("participant", new Participant());
        model.addAttribute("allCohorts", cohortService.showAllCohorts());
        return "add-participant";
    }

    @GetMapping(value = {"/instructor/add"})
    public String addInstructor(Model model){
        log.debug("Toevoegingspagina voor instructor opgevraagd");
        model.addAttribute("instructor", new Instructor());
        model.addAttribute("allCohorts", cohortService.showAllCohorts());
        return "add-instructor";
    }
}

