package com.corex.controller.user;

import com.corex.model.Loan;
import com.corex.util.SceneNavigator;

import java.util.ArrayList;
import java.util.List;

public class SavedLoansController {

    private final LoansController loansController = new LoansController();

    /** Only the loans the student has bookmarked. */
    public List<Loan> getSavedLoans() {
        List<Loan> saved = new ArrayList<>();
        for (Loan loan : loansController.getAllLoans()) {
            if (loansController.isSaved(loan.getId())) {
                saved.add(loan);
            }
        }
        return saved;
    }

    /** Takes the loan out of the saved list. */
    public void remove(Loan loan) {
        if (loansController.isSaved(loan.getId())) {
            loansController.toggleSaved(loan.getId());
        }
    }

    public void openDetails(Loan loan) {
        loansController.openDetails(loan);
    }

    public void openPage(String page) {
        SceneNavigator.go(page);
    }
}