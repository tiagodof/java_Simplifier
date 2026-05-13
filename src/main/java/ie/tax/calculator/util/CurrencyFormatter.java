package ie.tax.calculator.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class for formatting monetary values in Euro (EUR).
 */
public final class CurrencyFormatter {

    private static final NumberFormat EURO_FORMAT =
            NumberFormat.getCurrencyInstance(new Locale("en", "IE"));

    private CurrencyFormatter() {}

    /**
     * Formats a double value as a Euro currency string.
     * Example: 45000.5 -> "€45,000.50"
     */
    public static String format(double value) {
        return EURO_FORMAT.format(value);
    }

    /**
     * Formats a percentage value with two decimal places.
     * Example: 28.5 -> "28.50%"
     */
    public static String formatPercent(double value) {
        return String.format("%.2f%%", value);
    }
}
