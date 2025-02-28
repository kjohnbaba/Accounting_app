package com.cydeo.config;

import com.cydeo.annotations.EmailExist;
import com.cydeo.entity.User;
import com.cydeo.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmailValidator implements ConstraintValidator<EmailExist,String> {

    private final UserRepository userRepository;

    public EmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {

        List<String> existEmails= userRepository.findAll().stream().map(User::getUsername).collect(Collectors.toList());
        if (username.isEmpty()){
            return false;
        }
        if (existEmails.contains(username)){
            return false;
        }
        return true;
    }
}
