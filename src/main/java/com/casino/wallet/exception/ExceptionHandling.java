package com.casino.wallet.exception;

import com.casino.wallet.dto.response.Response;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandling {

    @ExceptionHandler(WalletException.EntityNotFoundException.class)
    public final ResponseEntity handleNotFountExceptions(Exception ex, WebRequest request) {
        Response response = Response.notFound();
        response.addErrorMsg(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WalletException.InternalException.class)
    public final ResponseEntity handleDeletionException(Exception ex, WebRequest request) {
        Response response = Response.internal();
        response.addErrorMsg(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WalletException.NotAllowedException.class)
    public final ResponseEntity handleNotAllowedException(Exception ex, WebRequest request) {
        Response response = Response.notAllowed();
        response.addErrorMsg(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(WalletException.DuplicateEntityException.class)
    public final ResponseEntity handleDuplicateException(Exception ex, WebRequest request) {
        Response response = Response.duplicateEntry();
        response.addErrorMsg(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        Response response = Response.validationException();
        List<String> details = new ArrayList<>();
        for (final ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        response.addErrorMsg(details.toString(), ex);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request){
        Response response = Response.badRequest();
        response.addErrorMsg(ex.getMessage(),ex);
        return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
    }
}
