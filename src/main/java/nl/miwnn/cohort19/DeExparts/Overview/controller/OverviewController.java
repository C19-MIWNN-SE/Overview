package nl.miwnn.cohort19.DeExparts.Overview.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
@RequestMapping("/overview")
@Controller
public class OverviewController {

    private static final Logger log =
            LoggerFactory.getLogger(OverviewController.class);

    @GetMapping(value = {"/", ""})
    public String showOverview(Model model) {
        log.debug("Overview pagina opgevraagd");
        model.addAttribute("title", "Deelnemer overzicht");
        return "overview";
    }
}
