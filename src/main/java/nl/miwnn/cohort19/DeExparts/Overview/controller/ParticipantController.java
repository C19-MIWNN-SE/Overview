package nl.miwnn.cohort19.DeExparts.Overview.controller;

import jakarta.persistence.Id;
import jakarta.validation.Valid;
import nl.miwnn.cohort19.DeExparts.Overview.model.Image;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
import nl.miwnn.cohort19.DeExparts.Overview.model.Role;
import nl.miwnn.cohort19.DeExparts.Overview.model.User;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.ImageRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.RoleRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.UserRepository;
import nl.miwnn.cohort19.DeExparts.Overview.service.CohortService;
import nl.miwnn.cohort19.DeExparts.Overview.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Handles requests related to participant management, including viewing,
 * adding, editing, deleting, and saving participant details and profile images.
 */
@Controller
@RequestMapping("/detail/participant")
public class ParticipantController {

    private static final Logger log =
            LoggerFactory.getLogger(ParticipantController.class);

    private final ParticipantService participantService;
    private final CohortService cohortService;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public ParticipantController(ParticipantService participantService, CohortService cohortService, ImageRepository imageRepository, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.participantService = participantService;
        this.cohortService = cohortService;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = {"/{id}"})
    public String showParticipantDetail(@PathVariable Long id,
                                        Model model,
                                        @AuthenticationPrincipal User currentUser) {
        Optional<Participant> participant = participantService.showParticipantDetail(id);
        log.debug("Detail pagina deelnemer opgevraagd");

        if (currentUser.isParticipant()){
            Long CurrentCohortId = currentUser.getParticipant().getCohorts().getId();
            Long OtherCohortId = participant.get().getCohorts().getId();
            if (!CurrentCohortId.equals(OtherCohortId)){
                model.addAttribute("bericht",
                        "Je kan alleen de pagina van een klasgenoot zien.");
                return "error/403";
            }
        }

        if (currentUser.isParticipant() && currentUser.getParticipant().getId().equals(id)) {
            model.addAttribute("activePage", "aboutMe");
        }

        model.addAttribute("title", "Detail overzicht");
        model.addAttribute("participant", participant.get());
        return "detail-participant";
    }

    @PostMapping(value = {"/delete/{id}"})
    public String deleteParticipant(@PathVariable Long id, @RequestParam Long cohortId){
        Optional<Participant> participant = participantService.showParticipantDetail(id);
        log.info("Verwijderaanvraag voor deelnemer met id: {} aangevraagd",id);
        if (participant.isEmpty()){
            log.warn("participant met id {} niet gevonden",id);
            return "redirect:/cohort";
        } else {
            participantService.deleteParticipant(id);
            log.info("Participant met id {} succesvol verwijderd.",id);
            return "redirect:/cohort/" + cohortId;
        }
    }

    @GetMapping(value = {"/edit/{id}"})
    public String editParticipant(@PathVariable Long id,
                                  Model model,
                                  @AuthenticationPrincipal User currentUser){
        Optional<Participant> participant = participantService.showParticipantDetail(id);
        log.debug("Bewerkingspagina voor participant met id {} opgevraagd", id);

        if (currentUser.isParticipant()){
            Long userId = currentUser.getParticipant().getId();
            if (!userId.equals(id)){
                model.addAttribute("bericht",
                        "Je kan alleen je eigen pagina wijzigen");
                return "error/403";
            }
        }

        model.addAttribute("participant", participant.get());
        model.addAttribute("allCohorts", cohortService.showAllCohorts());
        model.addAttribute("isInstructor", currentUser.isInstructor());

        return "edit-participant";
    }

    // TODO methode versimpelen
    @PostMapping(value = {"/save"})
    public String saveParticipant(@Valid @ModelAttribute Participant editedParticipant,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  Model model,
                                  @AuthenticationPrincipal User currentUser,
                                  @RequestParam("imageFile") MultipartFile imageFile,
                                  @RequestParam(value = "deleteImage", defaultValue = "false") boolean deleteImage)
            throws IOException {

        if (bindingResult.hasErrors()){
            log.warn("Validatiefouten bij het opslaan: {}", bindingResult.getErrorCount());
            model.addAttribute("participant", editedParticipant);
            model.addAttribute("allCohorts", cohortService.showAllCohorts());
            return "edit-participant";
        }

        if (editedParticipant.getId() != null) {
            Participant existingParticipant = participantService.findById(editedParticipant.getId());

            if (!currentUser.isInstructor()) {
                existingParticipant.setEmployer(editedParticipant.getEmployer());
                existingParticipant.setCity(editedParticipant.getCity());
                existingParticipant.setPhoneNumber(editedParticipant.getPhoneNumber());
                existingParticipant.setDescription(editedParticipant.getDescription());

                if (!imageFile.isEmpty()) {
                    Image image = new Image();
                    image.setData(imageFile.getBytes());
                    image.setContentType(imageFile.getContentType());
                    imageRepository.save(image);
                    existingParticipant.setImage(image);
                } else if (deleteImage) {
                    existingParticipant.setImage(null);
                }
            }

            if (currentUser.isInstructor()) {
                existingParticipant.setFirstName(editedParticipant.getFirstName());
                existingParticipant.setLastName(editedParticipant.getLastName());
                existingParticipant.setEmailAdress(editedParticipant.getEmailAdress());
                existingParticipant.setBirthDate(editedParticipant.getBirthDate());
                existingParticipant.setCohorts(editedParticipant.getCohorts());
            }

//            if (bindingResult.hasErrors()){
//                log.warn("Validatiefouten bij het opslaan: {}", bindingResult.getErrorCount());
//                model.addAttribute("participant", existingParticipant);
//                model.addAttribute("allCohorts", cohortService.showAllCohorts());
//                return "edit-participant";
//            }

            participantService.saveParticipant(existingParticipant);
            log.info("Deelnemer bijgewerkt: {}", existingParticipant.getFullName());
            String redirectUrl = UriComponentsBuilder.fromPath("/detail/participant/{id}")
                    .buildAndExpand(existingParticipant.getId()).toUriString();
            return "redirect:" + redirectUrl;
        }

        Role participantRole = roleRepository.findByAuthority("ROLE_PARTICIPANT")
                .orElseThrow();

        String username = editedParticipant.getFirstName();
        String password = "password";

        User user = new User(username, passwordEncoder.encode(password));
        user.setRoles(List.of(participantRole));
        userRepository.save(user);

        editedParticipant.setUser(user);

        if (!imageFile.isEmpty()) {
            Image image = new Image();
            image.setData(imageFile.getBytes());
            image.setContentType(imageFile.getContentType());
            imageRepository.save(image);
            editedParticipant.setImage(image);
        }

        participantService.saveParticipant(editedParticipant);
        redirectAttributes.addAttribute("id", editedParticipant.getId());
        log.info("Deelnemer met id {} opgeslagen.", editedParticipant.getId());
        return "redirect:/detail/participant/{id}";
    }

    @GetMapping(value = {"/add"})
    public String addParticipant(Model model, @AuthenticationPrincipal User currentUser) {
        log.debug("Toevoegingspagina voor participant met opgevraagd");
        model.addAttribute("participant", new Participant());
        model.addAttribute("allCohorts", cohortService.showAllCohorts());
        model.addAttribute("isInstructor", currentUser.isInstructor());
        return "add-participant";
    }
}


