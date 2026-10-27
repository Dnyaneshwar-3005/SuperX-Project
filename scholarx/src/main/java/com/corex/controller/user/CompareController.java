package com.corex.controller.user;

import com.corex.model.Loan;
import com.corex.util.SceneNavigator;

import java.util.List;

public class CompareController {

    // The four filter chips
    public static final String ALL_BANKS  = "All banks";
    public static final String GOVERNMENT = "Government banks";
    public static final String PRIVATE    = "Private banks";
    public static final String ABROAD     = "Study abroad";

    private final LoansController loansController = new LoansController();

    public List<Loan> getAllLoans() {
        return loansController.getAllLoans();
    }

    /** Should this loan be ticked when the given chip is chosen? */
    public boolean matchesFilter(Loan loan, String filter) {
        if (GOVERNMENT.equals(filter)) {
            return loan.getBankType().startsWith("Government");
        }
        if (PRIVATE.equals(filter)) {
            return loan.getBankType().startsWith("Private");
        }
        if (ABROAD.equals(filter)) {
            return !loan.isIndiaOnly();
        }
        return true;   // All banks
    }

    public void openDetails(Loan loan) {
        loansController.openDetails(loan);
    }

    public void openPage(String page) {
        SceneNavigator.go(page);
    }
}