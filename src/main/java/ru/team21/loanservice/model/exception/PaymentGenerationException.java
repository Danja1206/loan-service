package ru.team21.loanservice.model.exception;

import org.springframework.http.HttpStatus;

public class PaymentGenerationException extends PaymentScheduleException {

    public PaymentGenerationException(String message, Throwable cause) {
        super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
