package ie.tax.calculator.model;

/**
 * Holds the input data for a single taxpayer calculation.
 * For married couples with two incomes, create one profile per person.
 */
public class TaxProfile {

    private final double grossIncome;
    private final FilingStatus filingStatus;
    private final EmploymentType employmentType;
    private final int age;
    private final boolean hasSpouseIncome;
    private final double spouseGrossIncome;
    private final int taxYear;

    private TaxProfile(Builder builder) {
        this.grossIncome       = builder.grossIncome;
        this.filingStatus      = builder.filingStatus;
        this.employmentType    = builder.employmentType;
        this.age               = builder.age;
        this.hasSpouseIncome   = builder.hasSpouseIncome;
        this.spouseGrossIncome = builder.spouseGrossIncome;
        this.taxYear           = builder.taxYear;
    }

    public double getGrossIncome()       { return grossIncome; }
    public FilingStatus getFilingStatus() { return filingStatus; }
    public EmploymentType getEmploymentType() { return employmentType; }
    public int getAge()                  { return age; }
    public boolean hasSpouseIncome()     { return hasSpouseIncome; }
    public double getSpouseGrossIncome() { return spouseGrossIncome; }
    public int getTaxYear()              { return taxYear; }

    public static class Builder {
        private double grossIncome;
        private FilingStatus filingStatus = FilingStatus.SINGLE;
        private EmploymentType employmentType = EmploymentType.PAYE_EMPLOYEE;
        private int age = 30;
        private boolean hasSpouseIncome = false;
        private double spouseGrossIncome = 0.0;
        private int taxYear = 2025;

        public Builder grossIncome(double grossIncome) {
            if (grossIncome < 0) throw new IllegalArgumentException("Gross income cannot be negative.");
            this.grossIncome = grossIncome;
            return this;
        }

        public Builder filingStatus(FilingStatus filingStatus) {
            this.filingStatus = filingStatus;
            return this;
        }

        public Builder employmentType(EmploymentType employmentType) {
            this.employmentType = employmentType;
            return this;
        }

        public Builder age(int age) {
            if (age < 16 || age > 120) throw new IllegalArgumentException("Age must be between 16 and 120.");
            this.age = age;
            return this;
        }

        public Builder spouseGrossIncome(double spouseGrossIncome) {
            if (spouseGrossIncome < 0) throw new IllegalArgumentException("Spouse income cannot be negative.");
            this.spouseGrossIncome = spouseGrossIncome;
            this.hasSpouseIncome = spouseGrossIncome > 0;
            return this;
        }

        public Builder taxYear(int taxYear) {
            this.taxYear = taxYear;
            return this;
        }

        public TaxProfile build() {
            return new TaxProfile(this);
        }
    }
}
