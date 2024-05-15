package com.example.ievent.tokenparser;

/**
 * Represents an EQUAL expression in the grammar.
 * @author Haolin Li
 */
public class EqualExp extends Exp {
    private Exp left;
    private Exp right;

    public EqualExp(Exp left, Exp right){
        this.left = left;
        this.right = right;
    }
    @Override
    public String toString() {
        return "(" + left.toString() + " EQUAL " + right.toString() + ")";
    }

    public Exp getLeft() {
        return left;
    }

    public Exp getRight() {
        return right;
    }
}
