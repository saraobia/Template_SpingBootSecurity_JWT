package com.project.service;

import com.project.exception.*;
import com.project.model.User;
import com.project.repository.UserRepository;
import com.project.service.interfaces.UserFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserFunctions {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean login(String email, String password) throws UserNotFoundException, UserGenericsException, UserPasswordWrongException, UserMailWrongException {
        try {
            User user = getUserByEmail(email)
                       .orElseThrow(() -> new UserNotFoundException("Client not found for email: " + email));
            if (!user.getEmail().equals(email))
                throw new UserMailWrongException("Invalid email: " + email);

            if (!user.getPassword().equals(password))
                throw new UserPasswordWrongException("Invalid password for email: " + email);

        } catch (UserNotFoundException | UserMailWrongException | UserPasswordWrongException e) {
            throw e;
        } catch (Exception e) {
            throw new UserGenericsException("Login failed", e);
        }
        return true;
    }

    @Override
    public Optional<User> getUserByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return Optional.of(user);
    }

    @Override
    public Optional<User> getUserInfoById(int id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Error"));
        String idUser = user.getId().toString(); //UFF
        return Optional.of(user);
    }

    @Override
    public Optional<User> getUserInfoById(String id) throws UserNotFoundException {
        return Optional.empty();
    }
}
