package nl.miwnn.cohort19.DeExparts.Overview.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Author: Anouk de Vos
 * !! Doel voor de class !!
 */
@Controller
public class IndexController {

    private static final Logger log =
            LoggerFactory.getLogger(IndexController.class);

    @GetMapping("/login")
    public String showIndex(Model model) {
        log.debug("Indexpagina opgevraagd");
        return "index";
    }
}
