package ie.tax.calculator.model;

/**
 * Immutable result object returned by the tax calculation service.
 * All monetary values are in Euros, rounded to 2 decimal places.
 */
public class TaxResult {

    private final double grossIncome;
    private final double taxableIncome;
    private final double grossIncomeTax;
    private final double totalTaxCredits;
    private final double netIncomeTax;
    private final double usc;
    private final double prsi;
    private final double totalDeductions;
    private final double netIncome;
    private final int taxYear;
    private final FilingStatus filingStatus;
    private final EmploymentType employmentType;

    public TaxResult(
            double grossIncome, double taxableIncome,
            double grossIncomeTax, double totalTaxCredits, double netIncomeTax,
            double usc, double prsi,
            int taxYear, FilingStatus filingStatus, EmploymentType employmentType) {

        this.grossIncome     = round(grossIncome);
        this.taxableIncome   = round(taxableIncome);
        this.grossIncomeTax  = round(grossIncomeTax);
        this.totalTaxCredits = round(totalTaxCredits);
        this.netIncomeTax    = round(Math.max(0, netIncomeTax));
        this.usc             = round(usc);
        this.prsi            = round(prsi);
        this.totalDeductions = round(this.netIncomeTax + this.usc + this.prsi);
        this.netIncome       = round(grossIncome - this.totalDeductions);
        this.taxYear         = taxYear;
        this.filingStatus    = filingStatus;
        this.employmentType  = employmentType;
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public double getGrossIncome()     { return grossIncome; }
    public double getTaxableIncome()   { return taxableIncome; }
    public double getGrossIncomeTax()  { return grossIncomeTax; }
    public double getTotalTaxCredits() { return totalTaxCredits; }
    public double getNetIncomeTax()    { return netIncomeTax; }
    public double getUsc()             { return usc; }
    public double getPrsi()            { return prsi; }
    public double getTotalDeductions() { return totalDeductions; }
    public double getNetIncome()       { return netIncome; }
    public int getTaxYear()            { return taxYear; }
    public FilingStatus getFilingStatus()     { return filingStatus; }
    public EmploymentType getEmploymentType() { return employmentType; }

    public double getEffectiveTaxRate() {
        if (grossIncome == 0) return 0;
        return round((totalDeductions / grossIncome) * 100);
    }
}
