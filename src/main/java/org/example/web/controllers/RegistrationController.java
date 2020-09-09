package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.web.app.services.RegistrationService;
import org.example.web.dto.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    Logger logger = Logger.getLogger(RegistrationController.class);
    private RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public String registration(Model model) {
        model.addAttribute("userForm", new UserForm());
        logger.info("GET method registration");
        return "registration_page";
    }

    @PostMapping("/add")
    public String registrationNewUser(UserForm userForm) {
        if(registrationService.addUser(userForm)) {
            logger.info("new user with name: " + userForm.getUsername() + " was added");
        }
        return "redirect:/registration";
    }



}
