package nl.miwnn.cohort19.DeExparts.Overview.controller;

import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.service.InstructorService;
import nl.miwnn.cohort19.DeExparts.Overview.service.ParticipantService;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.CohortRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private final CohortRepository cohortRepository;

    public IntroductionController(InstructorService instructorService, ParticipantService participantService) {
        this.instructorService = instructorService;
        this.participantService = participantService;
        this.cohortRepository = cohortRepository;
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
        Optional<Participant> participant = introductionService.showParticipantDetail(id);
        log.info("Verwijderaanvraag voor deelnemer met id: {} aangevraagd",id);
        if (participant.isEmpty()){
            log.warn("participant met id {} niet gevonden",id);
            return "redirect:/participant/{id}";
        } else {
            introductionService.deleteParticipant(id);
            log.info("Participant met id {} succesvol verwijderd.",id);
            return "redirect:/overview/";
        }
    }

    @PostMapping(value = {"/instructor/delete/{id}"})
    public String deleteInstructor(@PathVariable Long id){
        Optional<Instructor> instructor = introductionService.showInstructorDetail(id);
        log.info("Verwijderaanvraag voor docent met id: {} aangevraagd",id);
        if (instructor.isEmpty()){
            log.warn("docent met id {} niet gevonden",id);
            return "redirect:/instructor/{id}";
        } else {
            introductionService.deleteInstructor(id);
            log.info("Instructor met id {} succesvol verwijderd.",id);
            return "redirect:/overview/";
        }
    }

    @GetMapping(value = {"/participant/edit/{id}"})
    public String editParticipant(@PathVariable Long id, Model model){
        Optional<Participant> participant = introductionService.showParticipantDetail(id);
        log.debug("Bewerkingspagina voor participant met id {} opgevraagd", id);
        model.addAttribute("participant", participant.get());
        return "edit-participant";
    }

    @PostMapping(value = {"/participant/save"})
    public String saveParticipant(@ModelAttribute Participant editedParticipant, RedirectAttributes redirectAttributes){
        log.info("Deelnemer met id {} opgeslagen.", editedParticipant.getId());
        introductionService.saveParticipant(editedParticipant);
        redirectAttributes.addAttribute("id", editedParticipant.getId());
        return "redirect:/detail/participant/{id}";
    }

    @GetMapping(value = {"/instructor/edit/{id}"})
    public String editInstructor(@PathVariable Long id, Model model){
        Optional<Instructor> instructor = introductionService.showInstructorDetail(id);
        log.debug("Bewerkingspagina voor instructor met id {} opgevraagd", id);
        model.addAttribute("instructor", instructor.get());
        model.addAttribute("allCohorts", cohortRepository.findAll());
        return "edit-instructor";
    }

    @PostMapping(value = {"/instructor/save"})
    public String saveInstructor(@ModelAttribute Instructor editedInstructor, RedirectAttributes redirectAttributes){
        log.info("Docent met id {} opgeslagen.", editedInstructor.getId());
        introductionService.saveInstructor(editedInstructor);
        redirectAttributes.addAttribute("id", editedInstructor.getId());
        return "redirect:/detail/instructor/{id}";
    }
}

