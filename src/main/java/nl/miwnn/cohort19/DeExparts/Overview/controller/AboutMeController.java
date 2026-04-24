package nl.miwnn.cohort19.DeExparts.Overview.controller;

import nl.miwnn.cohort19.DeExparts.Overview.model.User;
import nl.miwnn.cohort19.DeExparts.Overview.service.InstructorService;
import nl.miwnn.cohort19.DeExparts.Overview.service.ParticipantService;
import nl.miwnn.cohort19.DeExparts.Overview.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
@Controller
@RequestMapping("/detail")
public class AboutMeController {

    private static final Logger log =
            LoggerFactory.getLogger(AboutMeController.class);

    private final ParticipantService participantService;
    private final InstructorService instructorService;
    private final UserService userService;

    public AboutMeController(
            InstructorService instructorService,
            ParticipantService participantService,
            UserService userService) {
        this.instructorService = instructorService;
        this.participantService = participantService;
        this.userService = userService;
    }

    @GetMapping(value = {"/aboutme"})
    public String showAboutMePage(@AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser
            , Model model) {
        User currentUser = userService.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.debug("Detailpagina opgevraagd voor: {}", currentUser.getUsername());
        model.addAttribute("title", "Detail overzicht");
        model.addAttribute("activePage", "aboutMe");
        if (currentUser.isParticipant()) {
            participantService.findParticipantByUserId(currentUser.getId())
                    .ifPresent(participant ->
                        {model.addAttribute("participant", participant);
                        model.addAttribute("userType", "participant");});
            return "detail-participant";
        } else if (currentUser.isInstructor()) {
            instructorService.findInstructorByUserId(currentUser.getId())
                    .ifPresent(instructor ->
                        {model.addAttribute("instructor", instructor);
                        model.addAttribute("userType", "instructor");});
            return "detail-instructor";
        }
        return "home";
    }
}

