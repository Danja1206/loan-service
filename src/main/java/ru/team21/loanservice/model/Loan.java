package ru.team21.loanservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Max(40000000)
    @Min(100000)
    private int amount;

    @NotNull
    @Min(1)
    @Max(25)
    private double interestRate;

    @Min(1)
    @Max(84)
    private int termInMonth;

    private BigDecimal monthlyPayment;

    private Long userId;

    @OneToOne
    private Payment payment;


}
