package ru.team21.loanservice.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FinancialUtil {

    public static double calculateMonthlyPayment(int amount, double interestRate, int termForMonths) {
        double monthlyInterestRate = interestRate / 1200;
        return  (amount * monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -termForMonths)));

    }
//    public static double calculateMonthlyPayment(BigDecimal amount, BigDecimal interestRate, int termForMonths) {
//        BigDecimal monthlyInterestRate = interestRate.divide(BigDecimal.valueOf(1200),10, RoundingMode.HALF_UP);
//        MathContext mc = new MathContext(2);
//        BigDecimal numerator = amount.multiply(monthlyInterestRate);
//        BigDecimal base = BigDecimal.ONE.add(monthlyInterestRate);
//        BigDecimal denominator = BigDecimal.ONE.subtract(base.pow(exp)
//        return  (amount * monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -termForMonths)));
//
//    }

    public static BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal monthlyInterestRate, int termForMonths) {
        try {


            BigDecimal one = BigDecimal.ONE;
            BigDecimal multiplier = one.add(monthlyInterestRate).pow(termForMonths);
            BigDecimal denominator = one.subtract(BigDecimal.ONE.divide(multiplier, 20, RoundingMode.HALF_EVEN));
            return amount.multiply(monthlyInterestRate).divide(denominator, 20, RoundingMode.HALF_EVEN);
        } catch (ArithmeticException e) {
            throw new ArithmeticException(e.getMessage());
        }
    }

    public static double roundItUp(double value) {
        return Math.round(value * 100.0) / 100.0;
    }


}
