package ie.tax.calculator.service;

import ie.tax.calculator.model.EmploymentType;
import ie.tax.calculator.model.FilingStatus;
import ie.tax.calculator.model.TaxProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PrsiCalculator")
class PrsiCalculatorTest {

    private PrsiCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new PrsiCalculator();
    }

    private TaxProfile profile(double income, EmploymentType type) {
        return new TaxProfile.Builder()
                .grossIncome(income)
                .filingStatus(FilingStatus.SINGLE)
                .employmentType(type)
                .build();
    }

    @Test
    @DisplayName("Class A: income below weekly threshold pays no PRSI")
    void classABelowThreshold() {
        // Annual threshold = 352 * 52 = 18,304
        assertEquals(0.0, calculator.calculatePrsi(profile(18_000, EmploymentType.PAYE_EMPLOYEE)), 0.01);
    }

    @Test
    @DisplayName("Class A: income above threshold pays 4.1%")
    void classAAboveThreshold() {
        // 40,000 * 4.1% = 1,640
        assertEquals(1_640.0, calculator.calculatePrsi(profile(40_000, EmploymentType.PAYE_EMPLOYEE)), 0.01);
    }

    @Test
    @DisplayName("Class S (self-employed): pays 4.0% on all income")
    void classSRate() {
        // 50,000 * 4.0% = 2,000
        assertEquals(2_000.0, calculator.calculatePrsi(profile(50_000, EmploymentType.SELF_EMPLOYED)), 0.01);
    }

    @Test
    @DisplayName("Class S: minimum contribution of €500 applies on low income")
    void classSMinimumContribution() {
        // 5,000 * 4.0% = 200, but minimum is 500
        assertEquals(500.0, calculator.calculatePrsi(profile(5_000, EmploymentType.SELF_EMPLOYED)), 0.01);
    }

    @Test
    @DisplayName("Director pays Class S rate")
    void directorPaysClassS() {
        // 60,000 * 4.0% = 2,400
        assertEquals(2_400.0, calculator.calculatePrsi(profile(60_000, EmploymentType.DIRECTOR)), 0.01);
    }

    @Test
    @DisplayName("PRSI class label is correct for each employment type")
    void prsiClassLabels() {
        assertEquals("Class A", calculator.getPrsiClass(EmploymentType.PAYE_EMPLOYEE));
        assertEquals("Class S", calculator.getPrsiClass(EmploymentType.SELF_EMPLOYED));
        assertEquals("Class S", calculator.getPrsiClass(EmploymentType.DIRECTOR));
    }
}
