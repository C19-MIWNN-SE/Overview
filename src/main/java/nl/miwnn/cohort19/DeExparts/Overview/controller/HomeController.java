package nl.miwnn.cohort19.DeExparts.Overview.controller;

import nl.miwnn.cohort19.DeExparts.Overview.model.Instructor;
import nl.miwnn.cohort19.DeExparts.Overview.model.Participant;
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

import java.util.Optional;

/**
 * @author wat doe ik?
 */
@RequestMapping("/home")
@Controller
public class HomeController {
    private static final Logger log =
            LoggerFactory.getLogger(HomeController.class);

    private final ParticipantService participantService;
    private final InstructorService instructorService;
    private final UserService userService;

    public HomeController(ParticipantService participantService,
                          InstructorService instructorService,
                          UserService userService) {
        this.participantService = participantService;
        this.instructorService = instructorService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String showHomePage(@AuthenticationPrincipal User currentUser, Model model) {
        log.debug("Homepagina opgevraagd voor: {}", currentUser.getUsername());

        User user = (User) userService.loadUserByUsername(currentUser.getUsername());
        Long userId = user.getId();

        if (user.isParticipant()) {
            Optional<Participant> participant = participantService.findParticipantByUserId(userId);
            model.addAttribute("user", participant.get());
            model.addAttribute("userType", "participant");
        }

        if (user.isInstructor()) {
            Optional<Instructor> instructor = instructorService.findInstructorByUserId(userId);
            model.addAttribute("user", instructor.get());
            model.addAttribute("userType", "instructor");
        }

        model.addAttribute("title", "Homepagina");
        return "home";
    }

}
