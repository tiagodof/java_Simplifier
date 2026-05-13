package ie.tax.calculator.model;

/**
 * Represents the filing status of a taxpayer for Irish income tax purposes.
 * The filing status determines the standard rate cut-off point and available
 * tax credits as defined by Revenue Ireland.
 */
public enum FilingStatus {

    /** Single person with no qualifying children. */
    SINGLE("Single"),

    /** Single person qualifying for the Single Person Child Carer Credit. */
    SINGLE_PARENT("Single Parent (SPCCC)"),

    /** Married or civil partnership with one income. */
    MARRIED_ONE_INCOME("Married / Civil Partnership (one income)"),

    /** Married or civil partnership with two incomes (joint assessment). */
    MARRIED_TWO_INCOMES("Married / Civil Partnership (two incomes)"),

    /** Widowed person without qualifying children. */
    WIDOWED("Widowed");

    private final String displayName;

    FilingStatus(String displayName) {
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
