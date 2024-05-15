package com.example.ievent.tokenparser;

/**
 * Represents an OR expression in the grammar.
 * @author Haolin Li
 */
public class OrExp extends Exp {
    private Exp left;
    private Exp right;

    public OrExp(Exp left, Exp right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " OR " + right.toString() + ")";
    }

    public Exp getLeft() {
        return left;
    }

    public Exp getRight() {
        return right;
    }
}