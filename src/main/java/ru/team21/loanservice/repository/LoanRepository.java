package ru.team21.loanservice.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team21.loanservice.model.Loan;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> getLoansByUserId(Long userId);

    List<Loan> findAllByUserId(Long userId);
}
