package com.example.ievent.tokenparser;

public class ValueExp extends Exp {
    private int value;

    public ValueExp(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}