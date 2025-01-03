package com.inghub.loan_api.models.enums;

public enum NumberOfInstallments {
    SIX(6),
    NINE(9),
    TWELVE(12),
    TWENTY_FOUR(24);

    private final int value;

    NumberOfInstallments(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static NumberOfInstallments fromValue(int value) {
        for (NumberOfInstallments installment : NumberOfInstallments.values()) {
            if (installment.getValue() == value) {
                return installment;
            }
        }
        throw new IllegalArgumentException("Invalid installment number: " + value);
    }

    public static boolean isValidValue(int value) {
        for (NumberOfInstallments installment : NumberOfInstallments.values()) {
            if (installment.getValue() == value) {
                return true;
            }
        }
        return false;
    }

}
