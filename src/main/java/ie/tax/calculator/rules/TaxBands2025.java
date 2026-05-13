package ie.tax.calculator.rules;

import ie.tax.calculator.model.FilingStatus;
import ie.tax.calculator.model.EmploymentType;

/**
 * Tax rates, bands and credits for the 2025 Irish tax year.
 * All figures sourced from Revenue.ie.
 *
 * Income Tax:
 *   Standard rate: 20%
 *   Higher rate:   40%
 *
 * USC (Universal Social Charge):
 *   0.5% on first €12,012
 *   2.0% on next  €15,370  (up to €27,382)
 *   3.0% on next  €42,662  (up to €70,044)
 *   8.0% on balance
 *   Exempt if total income <= €13,000
 *
 * PRSI:
 *   Class A (PAYE employees): 4.1% on all earnings (if weekly earnings > €352)
 *   Class S (self-employed / directors): 4.0% on all income
 */
public final class TaxBands2025 {

    private TaxBands2025() {}

    // Income Tax rates
    public static final double STANDARD_RATE = 0.20;
    public static final double HIGHER_RATE   = 0.40;

    // Standard rate cut-off points (annual)
    public static final double CUTOFF_SINGLE                = 44_000.0;
    public static final double CUTOFF_SINGLE_PARENT         = 48_000.0;
    public static final double CUTOFF_MARRIED_ONE_INCOME    = 53_000.0;
    public static final double CUTOFF_MARRIED_TWO_INCOMES   = 53_000.0;
    public static final double CUTOFF_MARRIED_SPOUSE_EXTRA  = 35_000.0; // max transferable increase
    public static final double CUTOFF_WIDOWED               = 44_000.0;

    // Tax credits
    public static final double CREDIT_SINGLE_PERSON         = 2_000.0;
    public static final double CREDIT_MARRIED_PERSON        = 4_000.0;
    public static final double CREDIT_EMPLOYEE_PAYE         = 2_000.0;  // Employee Tax Credit
    public static final double CREDIT_EARNED_INCOME         = 1_775.0;  // Self-employed / directors
    public static final double CREDIT_SINGLE_PARENT_CHILD   = 1_750.0;  // SPCCC

    // USC thresholds (cumulative upper limits)
    public static final double USC_EXEMPT_THRESHOLD = 13_000.0;
    public static final double USC_BAND1_LIMIT      = 12_012.0;   // 0.5%
    public static final double USC_BAND2_LIMIT      = 27_382.0;   // 2.0% (12,012 + 15,370)
    public static final double USC_BAND3_LIMIT      = 70_044.0;   // 3.0% (27,382 + 42,662)
    // Balance above 70,044 at 8%

    public static final double USC_RATE_BAND1 = 0.005;
    public static final double USC_RATE_BAND2 = 0.020;
    public static final double USC_RATE_BAND3 = 0.030;
    public static final double USC_RATE_BAND4 = 0.080;

    // Reduced USC rates (age 70+ or medical card holders with income <= €60,000)
    public static final double USC_REDUCED_RATE_BAND1 = 0.005;
    public static final double USC_REDUCED_RATE_BAND2 = 0.020;
    public static final double USC_REDUCED_INCOME_LIMIT = 60_000.0;

    // PRSI
    public static final double PRSI_CLASS_A_RATE = 0.041;  // PAYE employees
    public static final double PRSI_CLASS_S_RATE = 0.040;  // Self-employed / directors
    public static final double PRSI_WEEKLY_THRESHOLD = 352.0;

    /**
     * Returns the standard rate cut-off point for the given filing status.
     * For married couples with two incomes, the base cut-off is €53,000 for
     * the higher earner; the lower earner can increase it by up to €35,000.
     */
    public static double getCutOffPoint(FilingStatus status) {
        switch (status) {
            case SINGLE:              return CUTOFF_SINGLE;
            case SINGLE_PARENT:       return CUTOFF_SINGLE_PARENT;
            case MARRIED_ONE_INCOME:  return CUTOFF_MARRIED_ONE_INCOME;
            case MARRIED_TWO_INCOMES: return CUTOFF_MARRIED_TWO_INCOMES;
            case WIDOWED:             return CUTOFF_WIDOWED;
            default:                  return CUTOFF_SINGLE;
        }
    }

    /**
     * Returns the personal tax credit for the given filing status.
     */
    public static double getPersonalCredit(FilingStatus status) {
        switch (status) {
            case MARRIED_ONE_INCOME:
            case MARRIED_TWO_INCOMES:
                return CREDIT_MARRIED_PERSON;
            default:
                return CREDIT_SINGLE_PERSON;
        }
    }

    /**
     * Returns the employment-related tax credit for the given employment type.
     */
    public static double getEmploymentCredit(EmploymentType type) {
        switch (type) {
            case PAYE_EMPLOYEE: return CREDIT_EMPLOYEE_PAYE;
            case SELF_EMPLOYED:
            case DIRECTOR:      return CREDIT_EARNED_INCOME;
            default:            return CREDIT_EMPLOYEE_PAYE;
        }
    }
}
