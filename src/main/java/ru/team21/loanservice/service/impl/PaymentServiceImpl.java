package ru.team21.loanservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.team21.loanservice.model.Loan;
import ru.team21.loanservice.model.Payment;
import ru.team21.loanservice.model.exception.PaymentGenerationException;
import ru.team21.loanservice.model.exception.PaymentNotFoundException;
import ru.team21.loanservice.model.exception.PaymentProcessingException;
import ru.team21.loanservice.repository.PaymentRepository;
import ru.team21.loanservice.service.BaseService;
import ru.team21.loanservice.service.interfaces.PaymentService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.team21.loanservice.util.FinancialUtil.calculateMonthlyPayment;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl extends BaseService implements PaymentService {

    private final PaymentRepository paymentRepository;


//    @Override
//    public Payment generatePayments(Loan loan) {
//
//        try {
//            logger.info("Generate payments schedule for {}", loan);
//
//
//            double monthlyInterestRate = (loan.getInterestRate() / 1200);
////            BigDecimal monthlyInterestRate = BigDecimal.valueOf(loan.getInterestRate()).
////                    divide(BigDecimal.valueOf(1200),10, RoundingMode.HALF_UP);
//            int numberOfPayments = loan.getTermInMonth();
//            int loanAmount = loan.getAmount();
////            BigDecimal loanAmount = BigDecimal.valueOf(loan.getAmount());
//
//            double monthlyPayment = (FinancialUtil.calculateMonthlyPayment(
//                    loanAmount, loan.getInterestRate(), numberOfPayments));
////            BigDecimal monthlyPayment = FinancialUtil.calculateMonthlyPayment(loanAmount,
////                    BigDecimal.valueOf(loan.getMonthlyPayment()), numberOfPayments);
//
//            double balance = loanAmount;
////            BigDecimal balance = loanAmount;
//            LocalDate paymentDate = LocalDate.now();
//
//            List<Payment.PaymentDetails> paymentDetailsList = new ArrayList<>();
//
//            for (int i = 1; i <= numberOfPayments; i++) {
//
//                double interest = balance * monthlyInterestRate;
////                BigDecimal interest = balance.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);
//                double principal = monthlyPayment - interest;
////                BigDecimal principal = monthlyPayment.subtract(interest).setScale(2, RoundingMode.HALF_UP);
////                balance = balance.subtract(principal).setScale(2, RoundingMode.HALF_UP);
//                balance -= principal;
//
//                Payment.PaymentDetails paymentDetails = new Payment.PaymentDetails();
//                paymentDetails.setPaymentId(i);
//                paymentDetails.setPaymentDate(paymentDate);
//                paymentDetails.setPaymentAmount(roundItUp(monthlyPayment));
//                paymentDetails.setInterest(roundItUp(interest));
//                paymentDetails.setRemainingBalance(roundItUp(balance));
//                paymentDetails.setPrincipal(roundItUp(principal));
//                paymentDetails.setPaid(false);
//
//                paymentDetailsList.add(paymentDetails);
//                paymentDate = paymentDate.plusMonths(1);
//            }
//
//
//            Payment paymentJSON = new Payment();
//            paymentJSON.setPaymentDetails(paymentDetailsList);
//            loan.setPayment(paymentJSON);
//
//            logger.info("Payment schedule generated successfully for {}", loan);
//            return paymentJSON;
//
//        } catch (Exception e) {
//            logger.error("Failed generate payment schedule for loan {}", loan, e);
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    @Override
//    public Payment getPaymentById(Long id) {
//
//        try {
//            logger.info("Get payment schedule by id {}", id);
//            return paymentRepository.findById(id)
//                    .orElseThrow(() -> {
//                        logger.warn("Payment with id {} not found", id);
//                        return new RuntimeException("Payment with id " + id + " not found");
//                    });
//        } catch (Exception e) {
//            logger.error("Failed retrieve payment schedule by id {}", id, e);
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    @Transactional
//    @Override
//    public void makePayment(Loan loan, double amount) {
//
//        try {
//            logger.info("Make payment of {} for loan {}", amount, loan);
//
////            BigDecimal remainingAmount = BigDecimal.valueOf(amount);
//
//            Payment payment = loan.getPayment();
//
//            for (Payment.PaymentDetails paymentDetails : payment.getPaymentDetails()) {
//
//                System.out.println(paymentDetails);
//
//                if (!paymentDetails.isPaid()) {
//
//                    double paymentAmount = paymentDetails.getPaymentAmount();
////                    BigDecimal paymentAmount = paymentDetails.getPaymentAmount();
//
//                    if (amount >= paymentAmount) {
////                    if (remainingAmount.compareTo(paymentAmount) >= 0) {
//                        paymentDetails.setPaid(true);
////                        paymentDetails.setPaymentAmount(BigDecimal.ZERO);
////                        paymentDetails.setInterest(BigDecimal.ZERO);
////                        paymentDetails.setPrincipal(BigDecimal.ZERO);
//                        paymentDetails.setPaymentAmount(0);
//                        paymentDetails.setInterest(0);
//                        paymentDetails.setPrincipal(0);
//                        amount -= paymentAmount;
////                        remainingAmount = remainingAmount.subtract(paymentAmount);
//                    } else {
////                        BigDecimal paymentRatio = remainingAmount.divide(paymentAmount, 10, RoundingMode.HALF_UP);
//                        double tempInterest = paymentDetails.getInterest() / paymentAmount;
//                        double tempPrincipal = paymentDetails.getPrincipal() / paymentAmount;
//
//                        double newPaymentAmount = paymentAmount - amount;
//                        double newPrincipal = paymentDetails.getPrincipal() - amount;
//                        double newRemainingBalance = paymentDetails.getRemainingBalance() - amount;
//
//                        double paidInterest = tempInterest * amount;
//                        double paidPrincipal = tempPrincipal * amount;
//
////                        BigDecimal paidInterest = paymentDetails.getInterest().multiply(
////                                paymentRatio).setScale(2, RoundingMode.HALF_UP);
////                        BigDecimal paidPrincipal = paymentDetails.getPrincipal().multiply(
////                                paymentRatio).setScale(2, RoundingMode.HALF_UP);
//
//                        paymentDetails.setPaymentAmount(roundItUp(newPaymentAmount));
//                        paymentDetails.setPrincipal(roundItUp(paymentDetails.getPrincipal() - paidPrincipal));
//                        paymentDetails.setInterest(roundItUp(paymentDetails.getInterest() - paidInterest));
//                        paymentDetails.setRemainingBalance(roundItUp(newRemainingBalance));
////                        paymentDetails.setPaymentAmount(paymentAmount.subtract(remainingAmount)
////                                .setScale(2, RoundingMode.HALF_UP));
////                        paymentDetails.setPrincipal(paymentDetails.getPrincipal().subtract(
////                                paidPrincipal).setScale(2, RoundingMode.HALF_UP));
////                        paymentDetails.setInterest(paymentDetails.getInterest().subtract(
////                                paidInterest).setScale(2, RoundingMode.HALF_UP));
////                        paymentDetails.setRemainingBalance(paymentDetails.getRemainingBalance().subtract(
////                                remainingAmount).setScale(2, RoundingMode.HALF_UP));
////                        remainingAmount = BigDecimal.ZERO;
//                        amount = 0;
//
//                    }
//
//                }
//            }
//
//            paymentRepository.save(payment);
//            logger.info("Payment processed generated successfully for loan {}", loan);
//
//        } catch (Exception e) {
//            logger.error("Failed make payment for loan {}", loan, e);
//            throw new RuntimeException(e);
//
//        }
//    }


    @Override
    public Payment generatePayments(Loan loan) {
        try {
            logger.info("Generate payments schedule for {}", loan);

            BigDecimal monthlyInterestRate = BigDecimal.valueOf(loan.getInterestRate())
                    .divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
            int numberOfPayments = loan.getTermInMonth();
            BigDecimal loanAmount = BigDecimal.valueOf(loan.getAmount());

            BigDecimal monthlyPayment = calculateMonthlyPayment(loanAmount, monthlyInterestRate, numberOfPayments);

            BigDecimal balance = loanAmount;
            LocalDate paymentDate = LocalDate.now();

            List<Payment.PaymentDetails> paymentDetailsList = new ArrayList<>();

            for (int i = 1; i <= numberOfPayments; i++) {
                BigDecimal interest = balance.multiply(monthlyInterestRate);
                BigDecimal principal = monthlyPayment.subtract(interest);
                balance = balance.subtract(principal);

                Payment.PaymentDetails paymentDetails = new Payment.PaymentDetails();
                paymentDetails.setPaymentId(i);
                paymentDetails.setPaymentDate(paymentDate);
                paymentDetails.setPaymentAmount(monthlyPayment.setScale(2, RoundingMode.HALF_UP));
                paymentDetails.setInterest(interest.setScale(2, RoundingMode.HALF_UP));
                paymentDetails.setPrincipal(principal.setScale(2, RoundingMode.HALF_UP));
                paymentDetails.setRemainingBalance(balance.setScale(2, RoundingMode.HALF_UP));
                paymentDetails.setPaid(false);

                paymentDetailsList.add(paymentDetails);
                paymentDate = paymentDate.plusMonths(1);
            }

//            if (balance.compareTo(BigDecimal.ZERO) != 0) {
//                Payment.PaymentDetails lastPayment = paymentDetailsList.get(paymentDetailsList.size() - 1);
//                lastPayment.setRemainingBalance(BigDecimal.ZERO);
//                lastPayment.setPrincipal(lastPayment.getPrincipal().add(balance).setScale(2, RoundingMode.HALF_EVEN));
//            }

            Payment payment = new Payment();
            payment.setPaymentDetails(paymentDetailsList);
            loan.setPayment(payment);

            logger.info("Payment schedule generated successfully for {}", loan);
            return payment;

        } catch (Exception e) {
            logger.error("Failed generate payment schedule for loan {}", loan, e);
            throw new PaymentGenerationException("failed generate payment schedule for loan " + loan, e);
        }
    }

    @Override
    public Payment getPaymentById(Long id) {
        try {
            logger.info("Get payment schedule by id {}", id);
            return paymentRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Payment with id {} not found", id);
                        return new PaymentNotFoundException(id);
                    });
        } catch (Exception e) {
            logger.error("Failed retrieve payment schedule by id {}", id, e);
            throw new PaymentProcessingException("failed retrieve payment schedule by id " + id, e);
        }
    }

    @Transactional
    @Override
    public void makePayment(Loan loan, double amount) {
        try {
            logger.info("Make payment of {} for loan {}", amount, loan);

            BigDecimal remainingAmount = BigDecimal.valueOf(amount);
            Payment payment = loan.getPayment();

            for (Payment.PaymentDetails paymentDetails : payment.getPaymentDetails()) {
                if (!paymentDetails.isPaid() && remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal paymentAmount = paymentDetails.getPaymentAmount();

                    if (remainingAmount.compareTo(paymentAmount) >= 0) {
                        paymentDetails.setPaid(true);
                        paymentDetails.setPaymentAmount(BigDecimal.ZERO);
                        paymentDetails.setInterest(BigDecimal.ZERO);
                        paymentDetails.setPrincipal(BigDecimal.ZERO);
                        remainingAmount = remainingAmount.subtract(paymentAmount);
                    } else {
                        BigDecimal paymentRatio = remainingAmount.divide(
                                paymentAmount, 10, RoundingMode.HALF_UP);
                        BigDecimal paidInterest = paymentDetails.getInterest().multiply(
                                paymentRatio).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal paidPrincipal = paymentDetails.getPrincipal().multiply(
                                paymentRatio).setScale(2, RoundingMode.HALF_UP);

                        paymentDetails.setPaymentAmount(paymentAmount.subtract(
                                remainingAmount).setScale(2, RoundingMode.HALF_UP));
                        paymentDetails.setInterest(paymentDetails.getInterest().subtract(
                                paidInterest).setScale(2, RoundingMode.HALF_UP));
                        paymentDetails.setPrincipal(paymentDetails.getPrincipal().subtract(
                                paidPrincipal).setScale(2, RoundingMode.HALF_UP));
                        paymentDetails.setRemainingBalance(paymentDetails.getRemainingBalance().subtract(
                                remainingAmount).setScale(2, RoundingMode.HALF_UP));

                        remainingAmount = BigDecimal.ZERO;
                    }
                }
            }

            paymentRepository.save(payment);
            logger.info("Payment processed successfully for loan {}", loan);

        } catch (Exception e) {
            logger.error("Failed to make payment for loan {}", loan, e);
            throw new PaymentProcessingException("Failed make payment for loan " + loan, e);
        }
    }
}
