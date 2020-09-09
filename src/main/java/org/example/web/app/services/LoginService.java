package org.example.web.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LoginService {
    private Logger logger  = Logger.getLogger(LoginService.class);
    private RegistrationRepository registrationRepository;

    @Autowired
    public LoginService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public boolean authenticate(UserForm userForm) {
        logger.info("try to auth with log = " + userForm.getUsername());
        return registrationRepository.checkAuth(userForm);
    }
}
