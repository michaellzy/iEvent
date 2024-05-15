package com.example.ievent.tokenparser;

/**
 * Represents an AND expression in the grammar.
 * @author Haolin Li
 */
public class AndExp extends Exp {
    private Exp left;
    private Exp right;

    public AndExp(Exp left, Exp right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " AND " + right.toString() + ")";
    }

    public Exp getLeft() {
        return left;
    }

    public Exp getRight() {
        return right;
    }
}
