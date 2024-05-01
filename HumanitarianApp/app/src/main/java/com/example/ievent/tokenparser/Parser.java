package com.example.ievent.tokenparser;

public class Parser {
    public static class IllegalProductionException extends IllegalArgumentException {
        public IllegalProductionException(String errorMessage) {
            super(errorMessage);
        }
    }
    private Tokenizer tokenizer;
    private Token currentToken;

    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        this.currentToken = tokenizer.current();
    }

    public Exp parse() {
        if (!tokenizer.hasNext()) return null;
        return parseExpression();
    }

    private Exp parseExpression() {
        Exp exp = parseTerm();
        while (currentToken != null && currentToken.getType() == Token.Type.OR) {
            tokenizer.next();
            currentToken = tokenizer.current();
            exp = new OrExp(exp, parseTerm());
        }
        return exp;
    }

    private Exp parseTerm() {
        Exp exp = parseFactor();
        while (currentToken != null && currentToken.getType() == Token.Type.AND) {
            Token op = currentToken;
            tokenizer.next();
            currentToken = tokenizer.current();
            exp = new AndExp(exp, parseFactor());
        }
        return exp;
    }

    private Exp parseFactor() {
        if (currentToken.getType() == Token.Type.LBRA) {
            tokenizer.next();
            currentToken = tokenizer.current();
            Exp exp = parseExpression();
            if (currentToken.getType() != Token.Type.RBRA) {
                throw new RuntimeException("Expected ')'");
            }
            tokenizer.next();
            currentToken = tokenizer.current();
            return exp;
        }
        return parseCondition();
    }
    private Exp parseCondition() {
        System.out.println("Parsing condition with identifier: " + currentToken.getToken());
        if (currentToken.getType() == Token.Type.STR) {
            String identifier = currentToken.getToken();
            tokenizer.next();
            if (!tokenizer.hasNext()) {
                return new VariableExp(identifier);
            }
            currentToken = tokenizer.current();
            System.out.println("Current token after identifier: " + currentToken.getToken());
            switch (currentToken.getType()) {
                case LESS:
                case MORE:
                    return parseComparison(identifier);
                case AND:
                case OR:
                    return parseLogicalOperation(identifier);
                default:
                    throw new IllegalProductionException("Expected logical or comparison operator after identifier but get "+ currentToken.getToken());
            }
        } else {
            throw new IllegalProductionException("Expected identifier");
        }
    }
private Exp parseComparison(String identifier) {
    Token op = currentToken;
    tokenizer.next();
    currentToken = tokenizer.current();

    System.out.println("Expecting integer, current token: " + currentToken.getToken() + ", Type: " + currentToken.getType());

    if (currentToken.getType() != Token.Type.DOUBLE) {
        throw new IllegalProductionException("Expected integer after comparison operator but have " + currentToken.getToken());
    }

    double value = Double.parseDouble(currentToken.getToken());
    Exp right = new ValueExp(value);

    tokenizer.next();
    if (tokenizer.hasNext()) {
        currentToken = tokenizer.current();
    }

    if (op.getType() == Token.Type.MORE) {
        return new MoreExp(new VariableExp(identifier), right);
    } else if (op.getType() == Token.Type.LESS) {
        return new LessExp(new VariableExp(identifier), right);
    } else {
        throw new IllegalProductionException("Expected More or Less expression");
    }
}

    private Exp parseLogicalOperation(String identifier) {
        VariableExp left = new VariableExp(identifier);
        tokenizer.next();
        if (!tokenizer.hasNext() || currentToken.getType() != Token.Type.AND) {
            throw new IllegalProductionException("Expected 'AND' after identifier");
        }
        tokenizer.next();
        currentToken = tokenizer.current();
        if (!tokenizer.hasNext()) {
            throw new IllegalProductionException("Expected expression after 'AND'");
        }

        Exp right = parseFactor();
        return new AndExp(left, right);
    }
}
