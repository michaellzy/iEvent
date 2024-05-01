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
        if (!tokenizer.hasNext()) return null; // 没有更多token，返回null
        return parseExpression();
    }

    private Exp parseExpression() {
        Exp exp = parseTerm();
        while (currentToken != null && currentToken.getType() == Token.Type.OR) {
            Token op = currentToken;
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
        // 检查当前token是否为标识符
        System.out.println("Parsing condition with identifier: " + currentToken.getToken());
        if (currentToken.getType() == Token.Type.STR) {
            String identifier = currentToken.getToken();
            tokenizer.next();  // 移动到下一个Token
            if (!tokenizer.hasNext()) {
                // 如果没有更多的Token，返回一个VariableExp，表示只根据这个变量进行搜索
                return new VariableExp(identifier);
            }
            currentToken = tokenizer.current();  // 更新当前Token
            System.out.println("Current token after identifier: " + currentToken.getToken());
            // 根据操作符选择逻辑分支
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
    tokenizer.next();  // 移动到下一个Token，应该是数字
    currentToken = tokenizer.current();  // 更新当前令牌到这个数字

    System.out.println("Expecting integer, current token: " + currentToken.getToken() + ", Type: " + currentToken.getType());

    if (currentToken.getType() != Token.Type.INT) {
        throw new IllegalProductionException("Expected integer after comparison operator but have " + currentToken.getToken());
    }

    int value = Integer.parseInt(currentToken.getToken());
    Exp right = new ValueExp(value);

    tokenizer.next();  // 处理完后，移动到下一个令牌
    if (tokenizer.hasNext()) {
        currentToken = tokenizer.current();  // 更新令牌
    }

    // 根据操作符类型创建相应的表达式
    if (op.getType() == Token.Type.MORE) {
        return new MoreExp(new VariableExp(identifier), right);
    } else {
        return new LessExp(new VariableExp(identifier), right);
    }
}

    private Exp parseLogicalOperation(String identifier) {
        // 从传入的标识符创建一个Exp节点
        VariableExp left = new VariableExp(identifier);
        // 移动到下一个Token，预期是AND操作符
        tokenizer.next();
        if (!tokenizer.hasNext() || currentToken.getType() != Token.Type.AND) {
            throw new IllegalProductionException("Expected 'AND' after identifier");
        }
        // 移动到AND操作符之后的元素
        tokenizer.next();
        currentToken = tokenizer.current();
        if (!tokenizer.hasNext()) {
            throw new IllegalProductionException("Expected expression after 'AND'");
        }
        // 这里我们简单地调用parseFactor来处理AND右边的表达式
        // 这是一个简化的假设，实际情况你可能需要根据具体的逻辑调整
        Exp right = parseFactor();
        // 创建并返回AndExp对象
        return new AndExp(left, right);
    }
}
