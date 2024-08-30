package ru.team21.loanservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.team21.loanservice.model.Loan;
import ru.team21.loanservice.service.interfaces.LoanService;
import ru.team21.loanservice.service.interfaces.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/team21/api/v2/loan")
@RequiredArgsConstructor
@Validated
public class LoanController {
    private final LoanService loanService;
    private final PaymentService paymentService;

    @PostMapping("/save")
    public ResponseEntity<Loan> save(@Valid @RequestBody Loan loan) {
        try {
            Loan savedLoan = loanService.saveLoan(loan);
            return ResponseEntity.ok(savedLoan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/calculate")
    public ResponseEntity<Loan> calculate(@Valid @RequestBody Loan loan) {
        loan = loanService.calculatePayment(loan);
        loan.setPayment(paymentService.generatePayments(loan));
        return ResponseEntity.ok(loan);
    }

    @PostMapping("/{loanId}/makePayment")
    public ResponseEntity<String> makePayment(@PathVariable Long loanId, @RequestParam double amount) {
        if (amount <= 0) {
            return ResponseEntity.badRequest().body("Amount must be greater than 0");
        }
        Loan loan = loanService.getLoanById(loanId);
        if (loan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan not found");
        }
        paymentService.makePayment(loan, amount);
        return ResponseEntity.ok("Payment made");
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long loanId) {
        Loan loan = loanService.getLoanById(loanId);
        if (loan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(loan);
    }

    @GetMapping("/userLoans")
    public ResponseEntity<List<Loan>> getUserLoans() {
        List<Loan> loans = loanService.getLoansByUserId();
        if (loans == null || loans.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(loans);
    }

    @DeleteMapping("/{loanId}")
    public ResponseEntity<Object> delete(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.deleteLoanById(loanId));
    }
}
