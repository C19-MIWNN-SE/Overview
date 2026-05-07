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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wat doe ik? // TODO change authorname
 * Handles requests regarding the users homepage
 */
@RequestMapping("/home")
@Controller
public class HomeController {
    private static final Logger log =
            LoggerFactory.getLogger(HomeController.class);

    private final ParticipantService participantService;
    private final InstructorService instructorService;

    public HomeController(ParticipantService participantService,
                          InstructorService instructorService) {
        this.participantService = participantService;
        this.instructorService = instructorService;
    }

    @GetMapping("/")
    public String showHomePage(@AuthenticationPrincipal User currentUser,
                               Model model) {

        log.debug("Homepagina opgevraagd voor: {}", currentUser.getUsername());

        if (currentUser.isParticipant()) {
            participantService.findParticipantByUserId(currentUser.getId())
                    .ifPresent(participant -> {
                        model.addAttribute("user", participant);
//                        model.addAttribute("instructors", instructorService.
//                                showAllInstructorsForCohort();
                        model.addAttribute("userType", "participant");
                    });
        } else if (currentUser.isInstructor()) {
            instructorService.findInstructorByUserId(currentUser.getId())
                    .ifPresent(instructor -> {
                        model.addAttribute("user", instructor);
                        model.addAttribute("userType", "instructor");
                    });
        }

        model.addAttribute("title", "Homepagina");
        return "home";
    }
}
