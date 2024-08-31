package ru.team21.loanservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.team21.loanservice.model.Loan;
import ru.team21.loanservice.service.interfaces.LoanService;
import ru.team21.loanservice.service.interfaces.PaymentService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoanControllerTest {


    private LoanService loanService;
    private PaymentService paymentService;
    private LoanController loanController;

    @BeforeEach
    void setUp() {
        loanService = Mockito.mock(LoanService.class);
        paymentService = Mockito.mock(PaymentService.class);
        loanController = new LoanController(loanService, paymentService);
    }

    @Test
    void save_ShouldReturnOk_WhenLoanIsSavedSuccessfully() {
        // Arrange
        Loan loan = new Loan();
        when(loanService.saveLoan(any(Loan.class))).thenReturn(loan);

        // Act
        ResponseEntity<Loan> response = loanController.save(loan);

        // Assert
        assertEquals(ResponseEntity.ok(loan), response);
        verify(loanService, times(1)).saveLoan(any(Loan.class));
    }

    @Test
    void save_ShouldReturnBadRequest_WhenExceptionIsThrown() {
        // Arrange
        Loan loan = new Loan();
        when(loanService.saveLoan(any(Loan.class))).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<Loan> response = loanController.save(loan);

        // Assert
        assertEquals(ResponseEntity.badRequest().build(), response);
        verify(loanService, times(1)).saveLoan(any(Loan.class));
    }

    @Test
    void makePayment_ShouldReturnOk_WhenPaymentIsMadeSuccessfully() {
        // Arrange
        Loan loan = new Loan();
        when(loanService.getLoanById(anyLong())).thenReturn(loan);

        // Act
        ResponseEntity<String> response = loanController.makePayment(1L, 100.0);

        // Assert
        assertEquals(ResponseEntity.ok("Payment made"), response);
        verify(paymentService, times(1)).makePayment(any(Loan.class), anyDouble());
    }

    @Test
    void makePayment_ShouldReturnBadRequest_WhenAmountIsLessThanOrEqualToZero() {
        // Act
        ResponseEntity<String> response = loanController.makePayment(1L, 0.0);

        // Assert
        assertEquals(ResponseEntity.badRequest().body("Amount must be greater than 0"), response);
        verify(paymentService, never()).makePayment(any(Loan.class), anyDouble());
    }

    @Test
    void makePayment_ShouldReturnNotFound_WhenLoanIsNotFound() {
        // Arrange
        when(loanService.getLoanById(anyLong())).thenReturn(null);

        // Act
        ResponseEntity<String> response = loanController.makePayment(1L, 100.0);

        // Assert
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan not found"), response);
        verify(paymentService, never()).makePayment(any(Loan.class), anyDouble());
    }
}
