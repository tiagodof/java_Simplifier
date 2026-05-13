package ie.tax.calculator.cli;

import ie.tax.calculator.model.EmploymentType;
import ie.tax.calculator.model.FilingStatus;
import ie.tax.calculator.model.TaxProfile;
import ie.tax.calculator.model.TaxResult;
import ie.tax.calculator.service.TaxCalculatorService;

import java.util.Scanner;

/**
 * Interactive command-line interface for the Irish Tax Calculator.
 * Guides the user through entering their details and displays a full
 * tax breakdown including Income Tax, USC and PRSI.
 */
public class TaxCalculatorCLI {

    private static final TaxCalculatorService service = new TaxCalculatorService();
    private static final TaxReportPrinter printer = new TaxReportPrinter();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   Irish Tax Calculator — Tax Year 2025   ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println("  Calculates Income Tax, USC and PRSI");
        System.out.println("  Source: Revenue.ie");
        System.out.println();

        boolean continueCalc = true;
        while (continueCalc) {
            TaxProfile profile = buildProfile(scanner);
            TaxResult result   = service.calculate(profile);
            printer.print(result);

            System.out.print("Calculate again? (y/n): ");
            String again = scanner.nextLine().trim().toLowerCase();
            continueCalc = again.equals("y");
        }

        System.out.println("Goodbye!");
        scanner.close();
    }

    private static TaxProfile buildProfile(Scanner scanner) {
        TaxProfile.Builder builder = new TaxProfile.Builder();

        // Gross income
        System.out.print("Enter your gross annual income (€): ");
        builder.grossIncome(parseDouble(scanner.nextLine()));

        // Filing status
        System.out.println("Filing status:");
        for (int i = 0; i < FilingStatus.values().length; i++) {
            System.out.printf("  %d. %s%n", i + 1, FilingStatus.values()[i].getDisplayName());
        }
        System.out.print("Select (1-" + FilingStatus.values().length + "): ");
        int fsChoice = parseInt(scanner.nextLine(), 1, FilingStatus.values().length);
        FilingStatus status = FilingStatus.values()[fsChoice - 1];
        builder.filingStatus(status);

        // Spouse income for married two incomes
        if (status == FilingStatus.MARRIED_TWO_INCOMES) {
            System.out.print("Enter spouse/partner gross annual income (€): ");
            builder.spouseGrossIncome(parseDouble(scanner.nextLine()));
        }

        // Employment type
        System.out.println("Employment type:");
        for (int i = 0; i < EmploymentType.values().length; i++) {
            System.out.printf("  %d. %s%n", i + 1, EmploymentType.values()[i].getDisplayName());
        }
        System.out.print("Select (1-" + EmploymentType.values().length + "): ");
        int etChoice = parseInt(scanner.nextLine(), 1, EmploymentType.values().length);
        builder.employmentType(EmploymentType.values()[etChoice - 1]);

        // Age
        System.out.print("Enter your age: ");
        builder.age(parseInt(scanner.nextLine(), 16, 120));

        return builder.build();
    }

    private static double parseDouble(String input) {
        try {
            double value = Double.parseDouble(input.trim().replace(",", ""));
            if (value < 0) throw new NumberFormatException();
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Using 0.");
            return 0.0;
        }
    }

    private static int parseInt(String input, int min, int max) {
        try {
            int value = Integer.parseInt(input.trim());
            if (value < min || value > max) throw new NumberFormatException();
            return value;
        } catch (NumberFormatException e) {
            System.out.printf("Invalid input. Using %d.%n", min);
            return min;
        }
    }
}
