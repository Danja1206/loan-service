package ru.team21.loanservice.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.team21.loanservice.model.Loan;
import ru.team21.loanservice.repository.LoanRepository;
import ru.team21.loanservice.repository.PaymentRepository;
import ru.team21.loanservice.service.BaseService;
import ru.team21.loanservice.service.JwtService;
import ru.team21.loanservice.service.interfaces.LoanService;
import ru.team21.loanservice.service.interfaces.PaymentService;
import ru.team21.loanservice.util.FinancialUtil;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl extends BaseService implements LoanService {

    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final RestTemplate restTemplate;
    private final JwtService jwtService;
    private WebClient.Builder webClientBuilder;

    @Override
    public Loan saveLoan(Loan loan) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getCredentials() instanceof String) {
                String token = (String) authentication.getCredentials();

                Long userId = jwtService.extractUserId(token);

                loan.setUserId(userId);
                loan = calculatePayment(loan);
                loan.setPayment(paymentService.generatePayments(loan));
                paymentRepository.save(loan.getPayment());
                return loanRepository.save(loan);
//
//                Long id = jwtService.extractUserId(token);
//                id = getUserIdFromUserService(id);
//                loan.setId(id);
            } else {
                logger.error("User id not found");
                return null;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Loan> getAllLoan() {
        try {
            return loanRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Loan calculatePayment(Loan loan) {
        try {
            BigDecimal calculated = FinancialUtil.calculateMonthlyPayment(
                    BigDecimal.valueOf(loan.getAmount()),
                    BigDecimal.valueOf(loan.getInterestRate()).divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP),
                    loan.getTermInMonth()
            );
            loan.setMonthlyPayment(calculated.setScale(2, RoundingMode.HALF_UP));
            return loan;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Loan getLoanById(Long id) {
        try {
            Optional<Loan> loan = loanRepository.findById(id);
            return loan.orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Loan> getLoansByUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getCredentials() instanceof String) {
                String token = (String) authentication.getCredentials();

                Long userId = jwtService.extractUserId(token);
                return loanRepository.findAllByUserId(userId);
            } else
                return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteLoanById(Long id) {
        try {
            Loan loan = loanRepository.findById(id).orElse(null);
            loanRepository.delete(loan);
            return "Deleted successfully";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
