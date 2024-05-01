package com.example.ievent.tokenparser;

public class VariableExp extends Exp {
    private String name;

    public VariableExp(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getVariableName() {
        return name;
    }
}
