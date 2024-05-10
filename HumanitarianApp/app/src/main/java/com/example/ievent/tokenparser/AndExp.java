package com.example.ievent.tokenparser;

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

    // @Override
    // public int evaluate(Map<String, Integer> context) {
    //     return left.evaluate(context) && right.evaluate(context) ? 1 : 0;
    // }
}
