package com.casino.wallet.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {
    private Status status;
    private T payload;
    private Object errors;

    public static <T> Response<T> ok() {
        Response<T> response = new Response<>();
        response.setStatus(Status.OK);
        return response;
    }

    public static <T> Response<T> badRequest() {
        Response<T> response = new Response<>();
        response.setStatus(Status.BAD_REQUEST);
        return response;
    }

    public static <T> Response<T> validationException() {
        Response<T> response = new Response<>();
        response.setStatus(Status.VALIDATION_EXCEPTION);
        return response;
    }

    public static <T> Response<T> notEnoughFunds() {
        Response<T> response = new Response<>();
        response.setStatus(Status.NOT_ENOUGH_FUNDS);
        return response;
    }

    public static <T> Response<T> internal() {
        Response<T> response = new Response<>();
        response.setStatus(Status.INTERNAL);
        return response;
    }

    public static <T> Response<T> notFound() {
        Response<T> response = new Response<>();
        response.setStatus(Status.NOT_FOUND);
        return response;
    }

    public static <T> Response<T> duplicateEntry() {
        Response<T> response = new Response<>();
        response.setStatus(Status.DUPLICATE_ENTRY);
        return response;
    }

    public static <T> Response<T> notAllowed() {
        Response<T> response = new Response<>();
        response.setStatus(Status.NOT_ALLOWED);
        return response;
    }

    public void addErrorMsg(String errorMsg, Exception ex) {
        ResponseError error = new ResponseError()
                .setMessage(errorMsg)
                .setDetails(ex.getMessage())
                .setTimestamp(Instant.now());
        setErrors(error);
    }

    public enum Status {
        OK, BAD_REQUEST, NOT_ALLOWED, VALIDATION_EXCEPTION, NOT_ENOUGH_FUNDS, INTERNAL, NOT_FOUND, DUPLICATE_ENTRY
    }
}