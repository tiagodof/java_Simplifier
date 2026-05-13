package ie.tax.calculator.service;

import ie.tax.calculator.model.TaxProfile;
import ie.tax.calculator.model.TaxResult;

/**
 * Orchestrates the full Irish tax calculation for a given taxpayer profile.
 * Combines Income Tax, USC and PRSI into a single TaxResult.
 */
public class TaxCalculatorService {

    private final IncomeTaxCalculator incomeTaxCalculator;
    private final UscCalculator uscCalculator;
    private final PrsiCalculator prsiCalculator;

    public TaxCalculatorService() {
        this.incomeTaxCalculator = new IncomeTaxCalculator();
        this.uscCalculator       = new UscCalculator();
        this.prsiCalculator      = new PrsiCalculator();
    }

    /**
     * Calculates the full tax liability for the given profile.
     *
     * @param profile the taxpayer profile
     * @return a TaxResult containing all calculated values
     */
    public TaxResult calculate(TaxProfile profile) {
        double grossIncomeTax  = incomeTaxCalculator.calculateGrossIncomeTax(profile);
        double totalCredits    = incomeTaxCalculator.calculateTaxCredits(profile);
        double netIncomeTax    = incomeTaxCalculator.calculateNetIncomeTax(profile);
        double usc             = uscCalculator.calculateUsc(profile);
        double prsi            = prsiCalculator.calculatePrsi(profile);

        return new TaxResult(
                profile.getGrossIncome(),
                profile.getGrossIncome(),
                grossIncomeTax,
                totalCredits,
                netIncomeTax,
                usc,
                prsi,
                profile.getTaxYear(),
                profile.getFilingStatus(),
                profile.getEmploymentType()
        );
    }
}
