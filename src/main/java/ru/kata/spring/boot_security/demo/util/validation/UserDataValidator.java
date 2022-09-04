package ru.kata.spring.boot_security.demo.util.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.kata.spring.boot_security.demo.util.exceptions.UserDataException;

import java.util.List;
@Component
public class UserDataValidator {


    public void validateUserFields(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder mess = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError err : errors) {
                mess
                        .append(err.getDefaultMessage())
                        .append("; ")
                        .append(System.getProperty("line.separator"));
            }
            throw new UserDataException(mess.toString());
        }
    }

    public void validateUserFields(String password, BindingResult bindingResult) {
        if (password.trim().equals("")) {
            throw new UserDataException("Должен быть пароль!");
        }
        validateUserFields(bindingResult);
    }

}

