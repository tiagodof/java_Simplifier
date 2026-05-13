package ie.tax.calculator.service;

import ie.tax.calculator.model.EmploymentType;
import ie.tax.calculator.model.TaxProfile;
import ie.tax.calculator.rules.TaxBands2025;

/**
 * Calculates the Pay Related Social Insurance (PRSI) contribution.
 *
 * PRSI Class A (PAYE employees): 4.1% on all earnings.
 *   Applies only if weekly earnings exceed €352 (annual: €18,304).
 *   Below this threshold, the employee pays 0% PRSI.
 *
 * PRSI Class S (self-employed and directors): 4.0% on all income.
 *   Minimum annual contribution of €500 applies.
 */
public class PrsiCalculator {

    private static final double ANNUAL_PRSI_THRESHOLD = TaxBands2025.PRSI_WEEKLY_THRESHOLD * 52;
    private static final double PRSI_CLASS_S_MINIMUM  = 500.0;

    /**
     * Calculates the annual PRSI contribution.
     *
     * @param profile the taxpayer profile
     * @return annual PRSI amount
     */
    public double calculatePrsi(TaxProfile profile) {
        double income = profile.getGrossIncome();

        if (profile.getEmploymentType() == EmploymentType.PAYE_EMPLOYEE) {
            return calculateClassA(income);
        } else {
            return calculateClassS(income);
        }
    }

    private double calculateClassA(double income) {
        if (income < ANNUAL_PRSI_THRESHOLD) {
            return 0.0;
        }
        return income * TaxBands2025.PRSI_CLASS_A_RATE;
    }

    private double calculateClassS(double income) {
        double prsi = income * TaxBands2025.PRSI_CLASS_S_RATE;
        return Math.max(prsi, PRSI_CLASS_S_MINIMUM);
    }

    /**
     * Returns the PRSI class label for display purposes.
     */
    public String getPrsiClass(EmploymentType type) {
        switch (type) {
            case PAYE_EMPLOYEE: return "Class A";
            case SELF_EMPLOYED:
            case DIRECTOR:      return "Class S";
            default:            return "Unknown";
        }
    }
}
