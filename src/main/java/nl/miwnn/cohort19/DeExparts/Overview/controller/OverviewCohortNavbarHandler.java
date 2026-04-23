package nl.miwnn.cohort19.DeExparts.Overview.controller;

import nl.miwnn.cohort19.DeExparts.Overview.model.User;
import nl.miwnn.cohort19.DeExparts.Overview.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

/**
 * @author Coen Cuppes
 * Handles references for the navbar
 */
@ControllerAdvice(assignableTypes = {AboutMeController.class, InstructorController.class, ParticipantController.class,
        HomeController.class, CohortController.class})
public class OverviewCohortNavbarHandler {
    private final UserService userService;

    public OverviewCohortNavbarHandler(UserService userService) {
        this.userService = userService;
    }

    // TODO think of better strategy to couple user to its cohort overview
    @ModelAttribute
    public void addCurrentUserCohortId(@AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser, Model model) {
        User currentUser = userService.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (currentUser.isParticipant()) {
            model.addAttribute("currentCohortId", currentUser.getParticipant().getCohorts().getId());
        } else if (currentUser.isInstructor()) {
            model.addAttribute("currentCohortId", "instructor-overview");
        }
    }
}
