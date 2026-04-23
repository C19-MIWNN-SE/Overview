package nl.miwnn.cohort19.DeExparts.Overview.controller;

import jakarta.validation.Valid;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
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
@RequestMapping("/detail/participant")
public class ParticipantController {

    private static final Logger log =
            LoggerFactory.getLogger(ParticipantController.class);

    private final ParticipantService participantService;
    private final CohortService cohortService;

    public ParticipantController(ParticipantService participantService, CohortService cohortService) {
        this.participantService = participantService;
        this.cohortService = cohortService;
    }

    @GetMapping(value = {"/{id}"})
    public String showParticipantDetail(@PathVariable Long id, Model model) {
        Optional<Participant> participant = participantService.showParticipantDetail(id);
        log.debug("Detail pagina deelnemer opgevraagd");
        model.addAttribute("title", "Detail overzicht");
        model.addAttribute("participant", participant.get());
        return "detail-participant";
    }

    @PostMapping(value = {"/delete/{id}"})
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

    @GetMapping(value = {"/edit/{id}"})
    public String editParticipant(@PathVariable Long id, Model model){
        Optional<Participant> participant = participantService.showParticipantDetail(id);
        log.debug("Bewerkingspagina voor participant met id {} opgevraagd", id);
        model.addAttribute("participant", participant.get());
        model.addAttribute("allCohorts", cohortService.showAllCohorts());
        return "edit-participant";
    }

    @PostMapping(value = {"/save"})
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

    @GetMapping(value = {"/add"})
    public String addParticipant(Model model) {
        log.debug("Toevoegingspagina voor participant met opgevraagd");
        model.addAttribute("participant", new Participant());
        model.addAttribute("allCohorts", cohortService.showAllCohorts());
        return "add-participant";
    }
}


