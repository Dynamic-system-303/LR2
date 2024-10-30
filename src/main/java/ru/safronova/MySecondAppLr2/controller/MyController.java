package ru.safronova.MySecondAppLr2.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.safronova.MySecondAppLr2.exception.UnsupportedCodeException;
import ru.safronova.MySecondAppLr2.exception.ValidationFailedException;
import ru.safronova.MySecondAppLr2.model.Request;
import ru.safronova.MySecondAppLr2.model.Response;
import ru.safronova.MySecondAppLr2.service.ValidationService;

import java.text.SimpleDateFormat;

@RestController
public class MyController {

    private final ValidationService validationService;

    @Autowired
    public MyController (ValidationService validationService){
        this.validationService = validationService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request, BindingResult bindingResult) {
//    ResponseEntity<Response> feedback(@RequestBody Request request) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(request.getSystemTime())
                .code("success")
                .errorCode("")
                .errorMessage("")
                .build();

        var status = HttpStatus.OK;
        try {
            validationService.isCodeValid(request);
            validationService.isValid(bindingResult);
        } catch (UnsupportedCodeException e) {
            response.setCode("failed");
            response.setErrorCode("UnsupportedCodeException");
            response.setErrorMessage("Неверный код");
            status = HttpStatus.BAD_REQUEST;
        } catch (ValidationFailedException validationFailedException) {
            response.setCode("failed");
            response.setErrorCode("ValidationException");
            response.setErrorMessage("Ошибка валидации");
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            response.setCode("failed");
            response.setErrorCode("UnknownException");
            response.setErrorMessage("Произошла непридвиденная ошибка");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, status);
    }
    
}
