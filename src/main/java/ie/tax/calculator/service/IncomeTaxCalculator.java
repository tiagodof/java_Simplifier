package ie.tax.calculator.service;

import ie.tax.calculator.model.FilingStatus;
import ie.tax.calculator.model.EmploymentType;
import ie.tax.calculator.model.TaxProfile;
import ie.tax.calculator.rules.TaxBands2025;

/**
 * Calculates Irish Income Tax (before credits) for a given gross income
 * and filing status, using the 2025 standard rate bands.
 */
public class IncomeTaxCalculator {

    /**
     * Calculates the gross income tax (before applying tax credits).
     *
     * For married couples with two incomes, the standard rate band of the
     * higher earner can be increased by up to €35,000 (capped at the lower
     * earner's income). This method handles that adjustment automatically.
     *
     * @param profile the taxpayer profile
     * @return gross income tax before credits
     */
    public double calculateGrossIncomeTax(TaxProfile profile) {
        double income   = profile.getGrossIncome();
        double cutOff   = TaxBands2025.getCutOffPoint(profile.getFilingStatus());

        if (profile.getFilingStatus() == FilingStatus.MARRIED_TWO_INCOMES
                && profile.hasSpouseIncome()) {
            double spouseIncome = profile.getSpouseGrossIncome();
            double increase = Math.min(spouseIncome, TaxBands2025.CUTOFF_MARRIED_SPOUSE_EXTRA);
            cutOff = Math.min(cutOff + increase, income); // cap at own income
        }

        if (income <= cutOff) {
            return income * TaxBands2025.STANDARD_RATE;
        }

        double standardBandTax = cutOff * TaxBands2025.STANDARD_RATE;
        double higherBandTax   = (income - cutOff) * TaxBands2025.HIGHER_RATE;
        return standardBandTax + higherBandTax;
    }

    /**
     * Calculates the total tax credits applicable to the profile.
     * Credits reduce the gross income tax on a euro-for-euro basis.
     */
    public double calculateTaxCredits(TaxProfile profile) {
        double credits = TaxBands2025.getPersonalCredit(profile.getFilingStatus());
        credits += TaxBands2025.getEmploymentCredit(profile.getEmploymentType());

        if (profile.getFilingStatus() == FilingStatus.SINGLE_PARENT) {
            credits += TaxBands2025.CREDIT_SINGLE_PARENT_CHILD;
        }

        return credits;
    }

    /**
     * Returns the net income tax after applying credits.
     * Cannot be negative (excess credits are not refunded).
     */
    public double calculateNetIncomeTax(TaxProfile profile) {
        double gross   = calculateGrossIncomeTax(profile);
        double credits = calculateTaxCredits(profile);
        return Math.max(0, gross - credits);
    }
}
