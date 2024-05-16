package com.example.ievent.tokenparser;

/**
 * Represents an variable expression in the grammar.
 * @author Haolin Li
 */
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
