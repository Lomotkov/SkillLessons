package org.example.web.app.services;

import org.example.web.dto.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private RegistrationRepository registrationRepository;

    @Autowired
    public RegistrationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public boolean addUser(UserForm userForm) {
        if (!userForm.getUsername().isEmpty() && !userForm.getPassword().isEmpty()) {
            registrationRepository.store(userForm);
            return true;
        }
        return false;
    }
}
