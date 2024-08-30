package ru.team21.loanservice.service.interfaces;

import ru.team21.loanservice.model.Loan;

import java.util.List;

public interface LoanService {
    Loan saveLoan(Loan loan);

    List<Loan> getAllLoan();

    Loan calculatePayment(Loan loan);

//    Loan calculateLoan(Loan loan);

    Loan getLoanById(Long id);

    List<Loan> getLoansByUserId();

    String deleteLoanById(Long id);;
}
