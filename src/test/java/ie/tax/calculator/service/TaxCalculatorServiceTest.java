package ie.tax.calculator.service;

import ie.tax.calculator.model.EmploymentType;
import ie.tax.calculator.model.FilingStatus;
import ie.tax.calculator.model.TaxProfile;
import ie.tax.calculator.model.TaxResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for TaxCalculatorService.
 * Expected values cross-checked against the PwC Ireland income tax calculator
 * (pwc.ie/issues/budget/income-tax-calculator.html) for tax year 2025.
 */
@DisplayName("TaxCalculatorService (integration)")
class TaxCalculatorServiceTest {

    private TaxCalculatorService service;

    @BeforeEach
    void setUp() {
        service = new TaxCalculatorService();
    }

    @Test
    @DisplayName("Single PAYE employee earning €40,000")
    void singlePayeEmployee40k() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(40_000)
                .filingStatus(FilingStatus.SINGLE)
                .employmentType(EmploymentType.PAYE_EMPLOYEE)
                .age(30)
                .build();

        TaxResult result = service.calculate(profile);

        assertEquals(40_000.0, result.getGrossIncome(), 0.01);
        // Gross IT: 40,000 * 20% = 8,000; credits = 4,000; net IT = 4,000
        assertEquals(4_000.0, result.getNetIncomeTax(), 0.01);
        // USC: 12,012*0.5% + (27,382-12,012)*2% + (40,000-27,382)*3%
        //    = 60.06 + 307.40 + 378.54 = 746.00
        assertEquals(746.00, result.getUsc(), 0.01);
        // PRSI: 40,000 * 4.1% = 1,640
        assertEquals(1_640.0, result.getPrsi(), 0.01);
        // Net income: 40,000 - 4,000 - 746 - 1,640 = 33,614
        assertEquals(33_614.0, result.getNetIncome(), 0.01);
    }

    @Test
    @DisplayName("Married one income earning €70,000")
    void marriedOneIncome70k() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(70_000)
                .filingStatus(FilingStatus.MARRIED_ONE_INCOME)
                .employmentType(EmploymentType.PAYE_EMPLOYEE)
                .age(40)
                .build();

        TaxResult result = service.calculate(profile);

        // Gross IT: 53,000 * 20% + 17,000 * 40% = 10,600 + 6,800 = 17,400
        assertEquals(17_400.0, result.getGrossIncomeTax(), 0.01);
        // Credits: 4,000 (married) + 2,000 (employee) = 6,000
        assertEquals(6_000.0, result.getTotalTaxCredits(), 0.01);
        // Net IT: 17,400 - 6,000 = 11,400
        assertEquals(11_400.0, result.getNetIncomeTax(), 0.01);
    }

    @Test
    @DisplayName("Self-employed earning €55,000")
    void selfEmployed55k() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(55_000)
                .filingStatus(FilingStatus.SINGLE)
                .employmentType(EmploymentType.SELF_EMPLOYED)
                .age(35)
                .build();

        TaxResult result = service.calculate(profile);

        // Credits: 2,000 (personal) + 1,775 (earned income) = 3,775
        assertEquals(3_775.0, result.getTotalTaxCredits(), 0.01);
        // PRSI: 55,000 * 4.0% = 2,200
        assertEquals(2_200.0, result.getPrsi(), 0.01);
    }

    @Test
    @DisplayName("Net income is always less than gross income")
    void netIncomeAlwaysLessThanGross() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(100_000)
                .filingStatus(FilingStatus.SINGLE)
                .employmentType(EmploymentType.PAYE_EMPLOYEE)
                .age(45)
                .build();

        TaxResult result = service.calculate(profile);
        assertTrue(result.getNetIncome() < result.getGrossIncome());
    }

    @Test
    @DisplayName("Effective tax rate is between 0% and 100%")
    void effectiveTaxRateInValidRange() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(80_000)
                .filingStatus(FilingStatus.SINGLE)
                .employmentType(EmploymentType.PAYE_EMPLOYEE)
                .age(30)
                .build();

        TaxResult result = service.calculate(profile);
        assertTrue(result.getEffectiveTaxRate() >= 0);
        assertTrue(result.getEffectiveTaxRate() <= 100);
    }

    @Test
    @DisplayName("Zero income produces zero deductions")
    void zeroIncomeZeroDeductions() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(0)
                .filingStatus(FilingStatus.SINGLE)
                .employmentType(EmploymentType.PAYE_EMPLOYEE)
                .age(30)
                .build();

        TaxResult result = service.calculate(profile);
        assertEquals(0.0, result.getTotalDeductions(), 0.01);
        assertEquals(0.0, result.getNetIncomeTax(), 0.01);
        assertEquals(0.0, result.getUsc(), 0.01);
        assertEquals(0.0, result.getPrsi(), 0.01);
    }
}
