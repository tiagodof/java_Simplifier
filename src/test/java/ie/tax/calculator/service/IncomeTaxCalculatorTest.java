package ie.tax.calculator.service;

import ie.tax.calculator.model.EmploymentType;
import ie.tax.calculator.model.FilingStatus;
import ie.tax.calculator.model.TaxProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("IncomeTaxCalculator")
class IncomeTaxCalculatorTest {

    private IncomeTaxCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new IncomeTaxCalculator();
    }

    // --- Gross Income Tax ---

    @Test
    @DisplayName("Single: income fully within standard rate band")
    void singleIncomeWithinStandardBand() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(30_000)
                .filingStatus(FilingStatus.SINGLE)
                .employmentType(EmploymentType.PAYE_EMPLOYEE)
                .build();
        // 30,000 * 20% = 6,000
        assertEquals(6_000.0, calculator.calculateGrossIncomeTax(profile), 0.01);
    }

    @Test
    @DisplayName("Single: income crosses into higher rate band")
    void singleIncomeAboveStandardBand() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(60_000)
                .filingStatus(FilingStatus.SINGLE)
                .employmentType(EmploymentType.PAYE_EMPLOYEE)
                .build();
        // 44,000 * 20% + 16,000 * 40% = 8,800 + 6,400 = 15,200
        assertEquals(15_200.0, calculator.calculateGrossIncomeTax(profile), 0.01);
    }

    @Test
    @DisplayName("Married one income: wider standard rate band")
    void marriedOneIncomeStandardBand() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(53_000)
                .filingStatus(FilingStatus.MARRIED_ONE_INCOME)
                .employmentType(EmploymentType.PAYE_EMPLOYEE)
                .build();
        // 53,000 * 20% = 10,600
        assertEquals(10_600.0, calculator.calculateGrossIncomeTax(profile), 0.01);
    }

    @Test
    @DisplayName("Married two incomes: spouse income increases cut-off point")
    void marriedTwoIncomesSpouseIncrease() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(70_000)
                .filingStatus(FilingStatus.MARRIED_TWO_INCOMES)
                .employmentType(EmploymentType.PAYE_EMPLOYEE)
                .spouseGrossIncome(20_000)
                .build();
        // cut-off = 53,000 + min(20,000, 35,000) = 73,000 but capped at income 70,000
        // so all 70,000 at 20% = 14,000
        assertEquals(14_000.0, calculator.calculateGrossIncomeTax(profile), 0.01);
    }

    @Test
    @DisplayName("Single parent: higher cut-off point applies")
    void singleParentHigherCutOff() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(48_000)
                .filingStatus(FilingStatus.SINGLE_PARENT)
                .employmentType(EmploymentType.PAYE_EMPLOYEE)
                .build();
        // 48,000 * 20% = 9,600
        assertEquals(9_600.0, calculator.calculateGrossIncomeTax(profile), 0.01);
    }

    // --- Tax Credits ---

    @Test
    @DisplayName("PAYE employee credits: personal + employee credit")
    void payeEmployeeCredits() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(40_000)
                .filingStatus(FilingStatus.SINGLE)
                .employmentType(EmploymentType.PAYE_EMPLOYEE)
                .build();
        // 2,000 (personal) + 2,000 (employee) = 4,000
        assertEquals(4_000.0, calculator.calculateTaxCredits(profile), 0.01);
    }

    @Test
    @DisplayName("Self-employed credits: personal + earned income credit")
    void selfEmployedCredits() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(40_000)
                .filingStatus(FilingStatus.SINGLE)
                .employmentType(EmploymentType.SELF_EMPLOYED)
                .build();
        // 2,000 (personal) + 1,775 (earned income) = 3,775
        assertEquals(3_775.0, calculator.calculateTaxCredits(profile), 0.01);
    }

    @Test
    @DisplayName("Married credits: married personal credit applies")
    void marriedPersonalCredit() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(50_000)
                .filingStatus(FilingStatus.MARRIED_ONE_INCOME)
                .employmentType(EmploymentType.PAYE_EMPLOYEE)
                .build();
        // 4,000 (married) + 2,000 (employee) = 6,000
        assertEquals(6_000.0, calculator.calculateTaxCredits(profile), 0.01);
    }

    @Test
    @DisplayName("Single parent: SPCCC credit included")
    void singleParentChildCarerCredit() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(40_000)
                .filingStatus(FilingStatus.SINGLE_PARENT)
                .employmentType(EmploymentType.PAYE_EMPLOYEE)
                .build();
        // 2,000 (personal) + 2,000 (employee) + 1,750 (SPCCC) = 5,750
        assertEquals(5_750.0, calculator.calculateTaxCredits(profile), 0.01);
    }

    // --- Net Income Tax ---

    @Test
    @DisplayName("Net income tax cannot be negative")
    void netIncomeTaxNotNegative() {
        TaxProfile profile = new TaxProfile.Builder()
                .grossIncome(5_000)
                .filingStatus(FilingStatus.SINGLE)
                .employmentType(EmploymentType.PAYE_EMPLOYEE)
                .build();
        // Gross tax = 1,000; credits = 4,000 -> net = 0 (not -3,000)
        assertEquals(0.0, calculator.calculateNetIncomeTax(profile), 0.01);
    }
}
