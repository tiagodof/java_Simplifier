package ie.tax.calculator.service;

import ie.tax.calculator.model.EmploymentType;
import ie.tax.calculator.model.FilingStatus;
import ie.tax.calculator.model.TaxProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UscCalculator")
class UscCalculatorTest {

    private UscCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new UscCalculator();
    }

    private TaxProfile profile(double income, int age) {
        return new TaxProfile.Builder()
                .grossIncome(income)
                .filingStatus(FilingStatus.SINGLE)
                .employmentType(EmploymentType.PAYE_EMPLOYEE)
                .age(age)
                .build();
    }

    @Test
    @DisplayName("Income at or below €13,000 is USC exempt")
    void uscExemptBelowThreshold() {
        assertEquals(0.0, calculator.calculateUsc(profile(13_000, 30)), 0.01);
    }

    @Test
    @DisplayName("Income just above exempt threshold pays USC")
    void uscAppliesAboveThreshold() {
        double usc = calculator.calculateUsc(profile(13_001, 30));
        assertTrue(usc > 0, "USC should be greater than zero above the exempt threshold");
    }

    @Test
    @DisplayName("Standard USC: income fully in band 1 (0.5%)")
    void uscBand1Only() {
        // Income must be above 13,000 to be taxable at all.
        // Let's use 13,001 so it's taxable, and check the calculation.
        // 12,012 * 0.5% = 60.06
        // (13,001 - 12,012) * 2% = 989 * 0.02 = 19.78
        // Total = 79.84
        assertEquals(79.84, calculator.calculateUsc(profile(13_001, 30)), 0.01);
    }

    @Test
    @DisplayName("Standard USC: income spanning bands 1 and 2")
    void uscBands1And2() {
        // 12,012 * 0.5% + (20,000 - 12,012) * 2% = 60.06 + 159.76 = 219.82
        assertEquals(219.82, calculator.calculateUsc(profile(20_000, 30)), 0.01);
    }

    @Test
    @DisplayName("Standard USC: income spanning all four bands")
    void uscAllFourBands() {
        double income = 80_000;
        // Band 1: 12,012 * 0.005 = 60.06
        // Band 2: (27,382 - 12,012) * 0.02 = 307.40
        // Band 3: (70,044 - 27,382) * 0.03 = 1,279.86
        // Band 4: (80,000 - 70,044) * 0.08 = 796.48
        // Total = 2,443.80
        assertEquals(2_443.80, calculator.calculateUsc(profile(income, 30)), 0.01);
    }

    @Test
    @DisplayName("Reduced USC: age 70+ with income <= €60,000")
    void uscReducedRateAge70() {
        // 12,012 * 0.5% + (40,000 - 12,012) * 2% = 60.06 + 559.76 = 619.82
        assertEquals(619.82, calculator.calculateUsc(profile(40_000, 72)), 0.01);
    }

    @Test
    @DisplayName("Age 70+ with income above €60,000 pays standard rates")
    void uscStandardRateAge70HighIncome() {
        // Income > 60,000 so standard rates apply even at age 70+
        double standard = calculator.calculateUsc(profile(65_000, 30));
        double age70    = calculator.calculateUsc(profile(65_000, 72));
        assertEquals(standard, age70, 0.01);
    }
}
