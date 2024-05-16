package com.example.ievent.tokenparser;

/**
 * Represents an LESS expression in the grammar.
 * @author Haolin Li
 */
public class LessExp extends Exp {
    private Exp left;
    private Exp right;

    public LessExp(Exp left, Exp right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " < " + right.toString() + ")";
    }

    public Object getLeft() {
        return left;
    }
    public Object getRight(){
        return right;
    }
}