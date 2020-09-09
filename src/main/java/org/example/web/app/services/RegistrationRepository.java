package org.example.web.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.UserForm;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RegistrationRepository implements ProjectRepository<UserForm> {

    private final Logger logger = Logger.getLogger(ProjectRepository.class);
    private final Map<String, UserForm> repo = new HashMap<>();

    @Override
    public List<UserForm> retrieveAll() {
        return null;
    }

    @Override
    public void store(UserForm userForm) {
        if (repo.get(userForm.getUsername()) == null) {
            logger.info("add new user:" + userForm.getUsername());
            userForm.setUserId(String.valueOf(userForm.hashCode()));
            repo.put(userForm.getUsername(), userForm);
        } else {
            logger.info("user with name: " + userForm.getUsername() + " already exist");
        }
    }

    @Override
    public boolean removeItemById(String loginIdToRemove) {
        return false;
    }

    public boolean checkAuth(UserForm userForm) {
        UserForm userToLogin = repo.get(userForm.getUsername());
        return userToLogin != null
                && userToLogin.getPassword().equals(userForm.getPassword());
    }
}
