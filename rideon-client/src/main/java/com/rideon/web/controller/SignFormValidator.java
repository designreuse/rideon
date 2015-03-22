/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.web.controller;

import com.rideon.model.dto.SignFormDto;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author Fer
 */
public class SignFormValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignFormValidator.class);
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    public boolean supports(Class<?> type) {
        return SignFormDto.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        ValidationUtils.rejectIfEmpty(errors, "nickName", "NotEmpty");
        ValidationUtils.rejectIfEmpty(errors, "password", "NotEmpty");
        ValidationUtils.rejectIfEmpty(errors, "confirmPassword", "NotEmpty");

        SignFormDto form = (SignFormDto) o;

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(form.getEmail());
        if (!matcher.matches()) {
            errors.rejectValue("username", "Email");
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "confirmPassword.notequal");
        }
    }
}
