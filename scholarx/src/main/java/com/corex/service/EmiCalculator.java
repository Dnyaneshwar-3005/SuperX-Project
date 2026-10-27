package com.corex.service;

public class EmiCalculator {

    /** The answer of one calculation. */
    public static class Result {
        private final double emi;
        private final double principal;
        private final double interest;
        private final double total;
        private final int months;

        public Result(double emi, double principal, double interest, double total, int months) {
            this.emi = emi;
            this.principal = principal;
            this.interest = interest;
            this.total = total;
            this.months = months;
        }

        public double getEmi()       { return emi; }
        public double getPrincipal() { return principal; }
        public double getInterest()  { return interest; }
        public double getTotal()     { return total; }
        public int getMonths()       { return months; }
    }

    /**
     * Standard EMI formula:  EMI = P x r x (1+r)^n / ((1+r)^n - 1)
     * P = loan amount, r = monthly rate, n = number of months.
     */
    public static Result calculate(double principal, double annualRatePercent, double years) {
        int months = Math.max(1, (int) Math.round(years * 12));
        double r = annualRatePercent / 12 / 100;

        double emi;
        if (r == 0) {
            emi = principal / months;
        } else {
            double growth = Math.pow(1 + r, months);
            emi = principal * r * growth / (growth - 1);
        }

        double total = emi * months;
        return new Result(emi, principal, total - principal, total, months);
    }
}