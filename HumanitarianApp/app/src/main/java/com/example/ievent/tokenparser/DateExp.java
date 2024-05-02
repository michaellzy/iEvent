package com.example.ievent.tokenparser;

public class DateExp extends Exp{
    private String name;
    public DateExp(String name) {
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
