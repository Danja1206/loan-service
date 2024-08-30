package ru.team21.loanservice.service.interfaces;

import ru.team21.loanservice.model.Loan;
import ru.team21.loanservice.model.Payment;

public interface PaymentService {
    Payment generatePayments(Loan loan);

    Payment getPaymentById(Long id);

    void makePayment(Loan loan, double amount);
}
