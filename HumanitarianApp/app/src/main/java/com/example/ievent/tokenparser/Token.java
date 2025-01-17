package com.example.ievent.tokenparser;

import java.util.Objects;

/**
 * Represents a token in the grammar.
 * @author Haolin Li
 */
public class Token {
    public enum Type{DOUBLE,STR,AND,OR,LESS,MORE,LBRA,RBRA,EQUAL,DATE}
    public static class IllegalTokenException extends IllegalArgumentException {
        public IllegalTokenException(String errorMessage) {
            super(errorMessage);
        }
    }
    private final String token;
    private final Type type;

    public Token(String token, Type type){
        this.token = token;
        this.type = type;
    }
    public String getToken(){ return token; }
    public Type getType() {
        return type;
    }
    @Override
    public String toString() {
        if (type == Type.DOUBLE) {
            return "DOUBLE(" + token + ")";
        } else {
            return type + "";
        }
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) return true; // Same hashcode.
        if (!(other instanceof Token)) return false; // Null or not the same type.
        return this.type == ((Token) other).getType() && this.token.equals(((Token) other).getToken()); // Values are the same.
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, type);
    }
}
