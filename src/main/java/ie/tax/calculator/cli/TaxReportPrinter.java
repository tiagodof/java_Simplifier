package ie.tax.calculator.cli;

import ie.tax.calculator.model.TaxResult;
import ie.tax.calculator.util.CurrencyFormatter;

/**
 * Formats and prints a TaxResult to standard output.
 */
public class TaxReportPrinter {

    private static final String LINE  = "=".repeat(50);
    private static final String DLINE = "-".repeat(50);

    public void print(TaxResult result) {
        System.out.println();
        System.out.println(LINE);
        System.out.printf("  Irish Tax Calculation — Tax Year %d%n", result.getTaxYear());
        System.out.println(LINE);
        System.out.printf("  Filing Status  : %s%n", result.getFilingStatus().getDisplayName());
        System.out.printf("  Employment     : %s%n", result.getEmploymentType().getDisplayName());
        System.out.println(DLINE);

        row("Gross Income",          result.getGrossIncome());
        System.out.println(DLINE);

        row("Income Tax (gross)",    result.getGrossIncomeTax());
        row("Tax Credits",          -result.getTotalTaxCredits());
        row("Income Tax (net)",      result.getNetIncomeTax());
        System.out.println(DLINE);

        row("USC",                   result.getUsc());
        row("PRSI",                  result.getPrsi());
        System.out.println(DLINE);

        row("Total Deductions",      result.getTotalDeductions());
        row("Net Income (take-home)", result.getNetIncome());
        System.out.println(DLINE);

        System.out.printf("  Effective Tax Rate : %s%n",
                CurrencyFormatter.formatPercent(result.getEffectiveTaxRate()));
        System.out.println(LINE);
        System.out.println();
    }

    private void row(String label, double value) {
        System.out.printf("  %-28s %s%n", label + ":", CurrencyFormatter.format(value));
    }
}
