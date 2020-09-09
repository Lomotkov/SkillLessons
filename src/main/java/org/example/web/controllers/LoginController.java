package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.web.dto.UserForm;
import org.example.web.app.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/login")
public class LoginController {

    private Logger logger = Logger.getLogger(LoginController.class);
    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String login(Model model) {
        logger.info("GET /login returns loginPages");
        model.addAttribute("userForm", new UserForm());
        return "login_page";
    }

    @PostMapping("/auth")
    public String authenticate(UserForm userForm) {
        if(loginService.authenticate(userForm)) {
            logger.info("login ok");
            return "redirect:/books/shelf";
        } else {
            return "redirect:/login";
        }
    }
}
