package ie.tax.calculator.service;

import ie.tax.calculator.model.TaxProfile;
import ie.tax.calculator.rules.TaxBands2025;

/**
 * Calculates the Universal Social Charge (USC) for a given income.
 *
 * USC applies to gross income above €13,000.
 * If total income is €13,000 or less, USC does not apply.
 * Reduced rates apply to individuals aged 70 or over, or medical card
 * holders, whose income does not exceed €60,000.
 */
public class UscCalculator {

    /**
     * Calculates the annual USC liability.
     *
     * @param profile the taxpayer profile
     * @return annual USC amount
     */
    public double calculateUsc(TaxProfile profile) {
        double income = profile.getGrossIncome();

        if (income <= TaxBands2025.USC_EXEMPT_THRESHOLD) {
            return 0.0;
        }

        if (isEligibleForReducedRates(profile)) {
            return calculateReducedUsc(income);
        }

        return calculateStandardUsc(income);
    }

    private double calculateStandardUsc(double income) {
        double usc = 0.0;

        // Band 1: 0.5% on first €12,012
        double band1 = Math.min(income, TaxBands2025.USC_BAND1_LIMIT);
        usc += band1 * TaxBands2025.USC_RATE_BAND1;

        if (income > TaxBands2025.USC_BAND1_LIMIT) {
            // Band 2: 2% on next €15,370 (up to €27,382)
            double band2 = Math.min(income, TaxBands2025.USC_BAND2_LIMIT) - TaxBands2025.USC_BAND1_LIMIT;
            usc += band2 * TaxBands2025.USC_RATE_BAND2;
        }

        if (income > TaxBands2025.USC_BAND2_LIMIT) {
            // Band 3: 3% on next €42,662 (up to €70,044)
            double band3 = Math.min(income, TaxBands2025.USC_BAND3_LIMIT) - TaxBands2025.USC_BAND2_LIMIT;
            usc += band3 * TaxBands2025.USC_RATE_BAND3;
        }

        if (income > TaxBands2025.USC_BAND3_LIMIT) {
            // Band 4: 8% on balance
            double band4 = income - TaxBands2025.USC_BAND3_LIMIT;
            usc += band4 * TaxBands2025.USC_RATE_BAND4;
        }

        return usc;
    }

    /**
     * Reduced USC: 0.5% on first €12,012, 2% on balance.
     * Applies to those aged 70+ or medical card holders with income <= €60,000.
     */
    private double calculateReducedUsc(double income) {
        double usc = 0.0;

        double band1 = Math.min(income, TaxBands2025.USC_BAND1_LIMIT);
        usc += band1 * TaxBands2025.USC_REDUCED_RATE_BAND1;

        if (income > TaxBands2025.USC_BAND1_LIMIT) {
            double band2 = income - TaxBands2025.USC_BAND1_LIMIT;
            usc += band2 * TaxBands2025.USC_REDUCED_RATE_BAND2;
        }

        return usc;
    }

    private boolean isEligibleForReducedRates(TaxProfile profile) {
        return profile.getAge() >= 70
                && profile.getGrossIncome() <= TaxBands2025.USC_REDUCED_INCOME_LIMIT;
    }
}
