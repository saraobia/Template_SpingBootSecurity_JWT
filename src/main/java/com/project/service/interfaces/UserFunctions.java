package com.project.service.interfaces;

import com.project.exception.UserGenericsException;
import com.project.exception.UserMailWrongException;
import com.project.exception.UserNotFoundException;
import com.project.exception.UserPasswordWrongException;
import com.project.model.User;

import java.util.Optional;

public interface UserFunctions {
    boolean login(String email, String password) throws UserNotFoundException, UserGenericsException, UserPasswordWrongException, UserMailWrongException;
    Optional<User> getUserByEmail(String email) throws UserNotFoundException;
    Optional<User> getUserInfoById(int id) throws UserNotFoundException;

    Optional<User> getUserInfoById(String id) throws UserNotFoundException;
}
