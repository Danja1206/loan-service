package ru.team21.loanservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team21.loanservice.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}