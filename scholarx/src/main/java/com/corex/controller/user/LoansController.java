package com.corex.controller.user;

import com.corex.config.AppConstants;
import com.corex.model.Loan;
import com.corex.util.SceneNavigator;
import com.corex.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class LoansController {

    private final List<Loan> allLoans = new ArrayList<>();

    public LoansController() {
        // Sample list. Later this will come from Firebase (LoanDao).
        allLoans.add(new Loan("sbi", "SBI", "SBI", "Student Loan Scheme",
                "Government bank", "India & abroad", false, "#E6EEFB", "#1A4FC0",
                "Your course and study location are covered",
                "Amount needed is within the bank's limit"));

        allLoans.add(new Loan("hdfc", "HD", "HDFC Bank", "Education Loan",
                "Private bank", "India & abroad", false, "#ECE8FA", "#5B3FD0",
                "Your course and study location are covered",
                "Co-applicant requirement can be met"));

        allLoans.add(new Loan("icici", "IC", "ICICI Bank", "Education Loan",
                "Private bank", "India & abroad", false, "#DCF3EE", "#0F766E",
                "Your course and study location are covered",
                "Amount needed is within the bank's limit"));

        allLoans.add(new Loan("axis", "AX", "Axis Bank", "Education Loan",
                "Private bank", "India & abroad", false, "#E6EEFB", "#1A4FC0",
                "Your course and study location are covered",
                "Co-applicant requirement can be met"));

        allLoans.add(new Loan("bob", "BB", "Bank of Baroda", "Baroda Vidya",
                "Government bank", "India only", true, "#ECE8FA", "#5B3FD0",
                "Open to students studying in India",
                "Amount needed is within the bank's limit"));

        allLoans.add(new Loan("pnb", "PN", "Punjab National Bank", "PNB Saraswati",
                "Government bank", "India only", true, "#DCF3EE", "#0F766E",
                "Open to students studying in India",
                "Co-applicant requirement can be met"));
    }

    /** Returns the loans that suit the chosen study location. */
    public List<Loan> findMatches(String studyLocation) {
        List<Loan> matches = new ArrayList<>();
        for (Loan loan : allLoans) {
            boolean studyingAbroad = "Abroad".equals(studyLocation);
            if (studyingAbroad && loan.isIndiaOnly()) {
                continue;   // India-only loans do not cover study abroad
            }
            matches.add(loan);
        }
        return matches;
    }
        /** Returns every loan in the list. */
    public List<Loan> getAllLoans() {
        return new ArrayList<>(allLoans);
    }

    public boolean isSaved(String loanId) {
        return SessionManager.isLoanSaved(loanId);
    }

    public boolean toggleSaved(String loanId) {
        return SessionManager.toggleSavedLoan(loanId);
    }

    public void openDetails(Loan loan) {
        SessionManager.setSelectedLoanId(loan.getId());
        SceneNavigator.go(AppConstants.PAGE_LOAN_DETAILS);
    }

    public void openPage(String page) {
        SceneNavigator.go(page);
    }
}