package com.corex.util;

import java.util.LinkedHashSet;
import java.util.Set;

public class SessionManager {

    private static String studentName = "Student Name";   // placeholder until Firebase login
    private static final Set<String> savedLoanIds = new LinkedHashSet<>();
    private static String selectedLoanId = null;

    // ---------- Student ----------
    public static String getStudentName() {
        return studentName;
    }

    public static void setStudentName(String name) {
        if (name != null && !name.isBlank()) {
            studentName = name.trim();
        }
    }

    /** "Student Name" becomes "SN". */
    public static String getInitials() {
        String[] parts = studentName.trim().split("\\s+");
        String first = parts[0].substring(0, 1);
        String last = (parts.length > 1) ? parts[parts.length - 1].substring(0, 1) : "";
        return (first + last).toUpperCase();
    }

    // ---------- Saved loans ----------
    public static boolean isLoanSaved(String loanId) {
        return savedLoanIds.contains(loanId);
    }

    /** Saves the loan if it was not saved, removes it if it was. Returns true if it is saved now. */
    public static boolean toggleSavedLoan(String loanId) {
        if (savedLoanIds.remove(loanId)) {
            return false;
        }
        savedLoanIds.add(loanId);
        return true;
    }

    public static Set<String> getSavedLoanIds() {
        return new LinkedHashSet<>(savedLoanIds);
    }

    // ---------- The loan whose details page is open ----------
    public static String getSelectedLoanId() {
        return selectedLoanId;
    }

    public static void setSelectedLoanId(String loanId) {
        selectedLoanId = loanId;
    }
}