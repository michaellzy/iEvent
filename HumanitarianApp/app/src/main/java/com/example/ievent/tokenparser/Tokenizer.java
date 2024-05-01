package com.example.ievent.tokenparser;

import java.util.Scanner;

public class Tokenizer {
    private String buffer;          // String to be transformed into tokens each time next() is called.
    private Token currentToken;     // The current token. The next token is extracted when next() is called.

    public Tokenizer(String text) {
        buffer = text;          // save input text (string)
        next();                 // extracts the first token.
    }
//    public void next(){
//        char firstChar = buffer.charAt(0);
//        int idx = 1;
//        if (firstChar == '+')
//            currentToken = new Token("+", Token.Type.AND);
//        else if (firstChar == '/')
//            currentToken = new Token("/", Token.Type.OR);
//        else if (firstChar == '<')
//            currentToken = new Token("<", Token.Type.LESS);
//        else if (firstChar == '>')
//            currentToken = new Token(">", Token.Type.MORE);
//        else if (firstChar == '(')
//            currentToken = new Token("(", Token.Type.LBRA);
//        else if (firstChar == ')')
//            currentToken = new Token(")", Token.Type.RBRA);
//        else if (Character.isDigit(firstChar)) {
//            StringBuilder number = new StringBuilder(String.valueOf(firstChar));
//            while (idx < buffer.length() && Character.isDigit(buffer.charAt(idx))) {
//                number.append(buffer.charAt(idx));
//                idx++;
//            }
//            currentToken = new Token(number.toString(), Token.Type.INT);
//        } else if (Character.isLetter(firstChar)) {
//            StringBuilder identifier = new StringBuilder(String.valueOf(firstChar));
//            while (idx < buffer.length() && Character.isLetter(buffer.charAt(idx))) {
//                identifier.append(buffer.charAt(idx));
//                idx++;
//            }
//            currentToken = new Token(identifier.toString(), Token.Type.STR);
//        } else {
//            throw new Token.IllegalTokenException("Invalid token found from the expression");
//        }
//    }
public void next() {
    if (buffer.isEmpty()) {
        currentToken = null;
        return;
    }
    buffer = buffer.trim();

    System.out.println("Buffer before processing: " + buffer);
    char firstChar = buffer.charAt(0);
    int idx = 1;

    if (firstChar == '+' || firstChar == '/' || firstChar == '<' || firstChar == '>' || firstChar == '(' || firstChar == ')') {
        currentToken = new Token(String.valueOf(firstChar), mapCharToType(firstChar));
        idx = 1; // these are single character tokens
    } else if (Character.isDigit(firstChar)) {
        StringBuilder number = new StringBuilder(String.valueOf(firstChar));
        while (idx < buffer.length() && Character.isDigit(buffer.charAt(idx))) {
            number.append(buffer.charAt(idx++));
        }
        currentToken = new Token(number.toString(), Token.Type.INT);
    } else if (Character.isLetter(firstChar)) {
        StringBuilder identifier = new StringBuilder(String.valueOf(firstChar));
        while (idx < buffer.length() && Character.isLetter(buffer.charAt(idx))) {
            identifier.append(buffer.charAt(idx++));
        }
        currentToken = new Token(identifier.toString(), Token.Type.STR);
    } else {
        throw new Token.IllegalTokenException("Invalid token found from the expression " + firstChar+" received");
    }

    // Correctly set idx for identifier tokens
    buffer = buffer.substring(idx);
    System.out.println("Buffer after processing: " + buffer);
    System.out.println("Current token: " + currentToken.getToken() + ", Type: " + currentToken.getType());
}

    private Token.Type mapCharToType(char ch) {
        switch (ch) {
            case '+': return Token.Type.AND;
            case '/': return Token.Type.OR;
            case '<': return Token.Type.LESS;
            case '>': return Token.Type.MORE;
            case '(': return Token.Type.LBRA;
            case ')': return Token.Type.RBRA;
            default:  return null; // should never happen
        }
    }

    public Token current() {
        return currentToken;
    }
    public boolean hasNext() {
        return currentToken != null;
    }
}
