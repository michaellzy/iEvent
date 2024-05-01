package com.example.ievent.tokenparser;

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

    // public int evaluate() {
    //     // 实现逻辑OR的求值逻辑
    // }
}