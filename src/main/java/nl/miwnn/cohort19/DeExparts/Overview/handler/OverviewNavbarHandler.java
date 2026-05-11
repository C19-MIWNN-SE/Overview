package nl.miwnn.cohort19.DeExparts.Overview.handler;

import nl.miwnn.cohort19.DeExparts.Overview.controller.*;
import nl.miwnn.cohort19.DeExparts.Overview.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class OverviewNavbarHandler {

    @ModelAttribute
    public void addCurrentUserCohortId(@AuthenticationPrincipal User currentUser, Model model) {
        if (currentUser.isParticipant()) {
            model.addAttribute("currentParticipantId", currentUser.getParticipant().getId());
            model.addAttribute("currentCohortId", currentUser.getParticipant().getCohorts().getId());
        } else if (currentUser.isInstructor()) {
            model.addAttribute("currentInstructorId", currentUser.getInstructor().getId());
            model.addAttribute("currentCohortId", "instructor-overview");
        }
    }
}
