package nl.miwnn.cohort19.DeExparts.Overview.controller;

import jakarta.validation.Valid;
import nl.miwnn.cohort19.DeExparts.Overview.model.*;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.ImageRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.RoleRepository;
import nl.miwnn.cohort19.DeExparts.Overview.repositories.UserRepository;
import nl.miwnn.cohort19.DeExparts.Overview.service.CohortService;
import nl.miwnn.cohort19.DeExparts.Overview.service.InstructorService;
import nl.miwnn.cohort19.DeExparts.Overview.service.UserService;
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
 * @author wat doe ik?
 */
@Controller
@RequestMapping("/detail/instructor")
public class InstructorController {
    private static final Logger log =
            LoggerFactory.getLogger(InstructorController.class);

    private final InstructorService instructorService;
    private final CohortService cohortService;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;


    public InstructorController(InstructorService instructorService, CohortService cohortService,
                                ImageRepository imageRepository, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.instructorService = instructorService;
        this.cohortService = cohortService;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = {"/{id}"})
    public String showInstructorDetail(@PathVariable Long id, Model model,
                                       @AuthenticationPrincipal User currentUser) {
        Optional<Instructor> instructor = instructorService.showInstructorDetail(id);

        log.debug("Detail pagina docent opgevraagd");

        model.addAttribute("title", "Detail overzicht");
        model.addAttribute("instructor", instructor.get());

        if (currentUser.isParticipant()) {
            model.addAttribute("userType", "participant");
        } else if (currentUser.isInstructor()) {
            model.addAttribute("userType", "instructor");
        }
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
                                 Model model,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 @RequestParam(value = "deleteImage", defaultValue = "false") boolean deleteImage)
            throws IOException {
        if (bindingResult.hasErrors()){
            log.warn("Validatiefouten bij het opslaan: {}", bindingResult.getErrorCount());
            model.addAttribute("participant", editedInstructor); // TODO attributeName aanpassen
            model.addAttribute("allCohorts", cohortService.showAllCohorts());
            return "edit-instructor";
        }

        if (editedInstructor.getId() != null) {
            Instructor existingInstructor = instructorService.findById(editedInstructor.getId());
            existingInstructor.setCity(editedInstructor.getCity());
            existingInstructor.setPhoneNumber(editedInstructor.getPhoneNumber());

            if (!imageFile.isEmpty()) {
                Image image = new Image();
                image.setData(imageFile.getBytes());
                image.setContentType(imageFile.getContentType());
                imageRepository.save(image);
                existingInstructor.setImage(image);
            } else if (deleteImage) {
                existingInstructor.setImage(null);
            }

            instructorService.saveInstructor(existingInstructor);
            log.info("Deelnemer bijgewerkt: {}", existingInstructor.getFullName());
            String redirectUrl = UriComponentsBuilder.fromPath("/detail/instructor/{id}")
                    .buildAndExpand(existingInstructor.getId()).toUriString();
            return "redirect:" + redirectUrl;
        }

        Role instructorRole = roleRepository.findByAuthority("ROLE_INSTRUCTOR")
                .orElseThrow();

        String username = editedInstructor.getFirstName();
        String password = "pw";

        User user = new User(username, passwordEncoder.encode(password));
        user.setRoles(List.of(instructorRole));
        userRepository.save(user);

        editedInstructor.setUser(user);

        if (!imageFile.isEmpty()) {
            Image image = new Image();
            image.setData(imageFile.getBytes());
            image.setContentType(imageFile.getContentType());
            imageRepository.save(image);
            editedInstructor.setImage(image);
        }

        instructorService.saveInstructor(editedInstructor);
        redirectAttributes.addAttribute("id", editedInstructor.getId());
        log.info("Docent met id {} opgeslagen.", editedInstructor.getId());
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
