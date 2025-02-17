package com.anuragxone.test.one;

public class Payment {
    private final String type;
    private final double amount;
    private String provider = null;
    private String reference = null;

    public Payment(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public Payment(String type, double amount, String provider, String reference) {
        this.type = type;
        this.amount = amount;
        this.provider = provider;
        this.reference = reference;

    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}
