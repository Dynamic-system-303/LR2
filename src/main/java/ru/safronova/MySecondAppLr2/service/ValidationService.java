package ru.safronova.MySecondAppLr2.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.safronova.MySecondAppLr2.exception.UnsupportedCodeException;
import ru.safronova.MySecondAppLr2.exception.ValidationFailedException;
import ru.safronova.MySecondAppLr2.model.Request;


@Service
public interface ValidationService {
    void isValid(BindingResult bindingResult) throws ValidationFailedException;
    void isCodeValid(Request request) throws UnsupportedCodeException;
}
