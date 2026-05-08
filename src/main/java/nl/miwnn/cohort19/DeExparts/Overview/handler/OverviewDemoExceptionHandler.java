package nl.miwnn.cohort19.DeExparts.Overview.handler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

/**
 * Author: Anouk de Vos
 * Global exception handler that catches HTTP status exceptions and general
 * exceptions, returning appropriate error views (404, 500) with relevant messages.
 */

@ControllerAdvice
public class OverviewDemoExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public String handleNotFound(
            ResponseStatusException exception,
            Model model) {
        model.addAttribute("statusCode",
                exception.getStatusCode().value());
        model.addAttribute("bericht", exception.getReason());
        return "error/404";
    }
    @ExceptionHandler(Exception.class)
    public String handleAlgemeneUitzondering(
            Exception exception,
            Model model) {
        model.addAttribute("bericht", exception.getMessage());
        return "error/500";
    }
}
