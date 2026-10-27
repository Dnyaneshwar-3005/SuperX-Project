package com.corex.controller.user;

import com.corex.model.Loan;
import com.corex.util.LinkOpener;
import com.corex.util.SceneNavigator;
import com.corex.util.SessionManager;

public class LoanDetailsController {

    private final LoansController loansController = new LoansController();

    /** The loan whose "View details" button was clicked. Null if none was chosen. */
    public Loan getSelectedLoan() {
        String id = SessionManager.getSelectedLoanId();
        if (id == null) {
            return null;
        }
        for (Loan loan : loansController.getAllLoans()) {
            if (loan.getId().equals(id)) {
                return loan;
            }
        }
        return null;
    }

    public boolean isSaved(Loan loan) {
        return loansController.isSaved(loan.getId());
    }

    public boolean toggleSaved(Loan loan) {
        return loansController.toggleSaved(loan.getId());
    }

    /** Opens the bank's official website. Returns false if we have no verified link yet. */
    public boolean openBankWebsite(Loan loan) {
        String url = loan.getOfficialUrl();
        if (url == null || url.isBlank()) {
            return false;
        }
        return LinkOpener.open(url);
    }

    public void openPage(String page) {
        SceneNavigator.go(page);
    }
}