package com.cookbooker.objects;

import java.util.Locale;

public class Ingredient {
    private float amount;
    private String unit;
    private String substance;

    public Ingredient(float amount, String unit, String substance) {
        this.unit = unit;
        this.amount = amount;
        this.substance = substance;
    }

    public String unit() {
        return unit;
    }

    public float amount() {
        return amount;
    }

    public String substance() {
        return substance;
    }

    @Override
    public String toString() {
        return String.format(Locale.CANADA, "Ingredient:\n%.3f\t%s\t%s", amount, unit, substance);
    }
}
