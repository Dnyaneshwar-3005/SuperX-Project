package com.corex.model;

public class Loan {

    private final String id;
    private final String shortCode;     // letters shown in the badge, e.g. SBI
    private final String bankName;
    private final String productName;
    private final String bankType;      // Government bank / Private bank
    private final String coverage;      // India & abroad / India only
    private final boolean indiaOnly;
    private final String badgeTint;     // badge background color
    private final String badgeColor;    // badge text color
    private final String point1;        // first line in the check list
    private final String point2;        // second line in the check list

    // Placeholders for now. Verified bank data will be filled in later.
    private String rate = "[Rate]";
    private String maxAmount = "[Amount]";
    private String tenure = "[00] years";

    private String collateral = "[Details]";
    private String processingFee = "[Fee]";
    private String repaymentHoliday = "[Details]";
    private String officialUrl = "";   // add a verified https:// link here later

    public Loan(String id, String shortCode, String bankName, String productName,
                String bankType, String coverage, boolean indiaOnly,
                String badgeTint, String badgeColor, String point1, String point2) {
        this.id = id;
        this.shortCode = shortCode;
        this.bankName = bankName;
        this.productName = productName;
        this.bankType = bankType;
        this.coverage = coverage;
        this.indiaOnly = indiaOnly;
        this.badgeTint = badgeTint;
        this.badgeColor = badgeColor;
        this.point1 = point1;
        this.point2 = point2;
    }

    public String getId()          { return id; }
    public String getShortCode()   { return shortCode; }
    public String getBankName()    { return bankName; }
    public String getProductName() { return productName; }
    public String getBankType()    { return bankType; }
    public String getCoverage()    { return coverage; }
    public boolean isIndiaOnly()   { return indiaOnly; }
    public String getBadgeTint()   { return badgeTint; }
    public String getBadgeColor()  { return badgeColor; }
    public String getPoint1()      { return point1; }
    public String getPoint2()      { return point2; }
    public String getRate()        { return rate; }
    public String getMaxAmount()   { return maxAmount; }
    public String getTenure()      { return tenure; }

    public String getCollateral()       { return collateral; }
    public String getProcessingFee()    { return processingFee; }
    public String getRepaymentHoliday() { return repaymentHoliday; }
    public String getOfficialUrl()      { return officialUrl; }

    public void setRate(String rate)           { this.rate = rate; }
    public void setMaxAmount(String maxAmount) { this.maxAmount = maxAmount; }
    public void setTenure(String tenure)       { this.tenure = tenure; }

  
}