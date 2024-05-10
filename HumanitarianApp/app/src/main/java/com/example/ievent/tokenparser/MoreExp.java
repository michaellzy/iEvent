package com.example.ievent.tokenparser;

public class MoreExp extends Exp {
    private Exp left;
    private Exp right;

    public MoreExp(Exp left, Exp right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " > " + right.toString() + ")";
    }

    public Object getLeft() {
        return left;
    }
    public Object getRight(){
        return right;
    }

    // public int evaluate() {
    //     // 实现逻辑OR的求值逻辑
    // }
}