package com.example.ievent.tokenparser;

public class ValueExp extends Exp {
    private double value;

    public ValueExp(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public Object getValue() {
        return value;
    }
}