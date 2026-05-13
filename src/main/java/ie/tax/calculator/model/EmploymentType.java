package ie.tax.calculator.model;

/**
 * Represents the employment type of a taxpayer.
 * This affects the available tax credits and PRSI class.
 */
public enum EmploymentType {

    /**
     * PAYE employee (Pay As You Earn).
     * Entitled to the Employee Tax Credit (formerly PAYE credit) of €2,000.
     * Pays PRSI Class A.
     */
    PAYE_EMPLOYEE("PAYE Employee"),

    /**
     * Self-employed individual.
     * Entitled to the Earned Income Credit of €1,775 instead of the Employee Credit.
     * Pays PRSI Class S (4%).
     */
    SELF_EMPLOYED("Self-Employed"),

    /**
     * Director of a company.
     * Treated similarly to self-employed for tax credit purposes.
     * Pays PRSI Class S.
     */
    DIRECTOR("Company Director");

    private final String displayName;

    EmploymentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
