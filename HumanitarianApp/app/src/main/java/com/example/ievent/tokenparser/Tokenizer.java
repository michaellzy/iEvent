package com.example.ievent.tokenparser;
import android.util.Log;

public class Tokenizer {
    private String buffer;          // String to be transformed into tokens each time next() is called.
    private Token currentToken;     // The current token. The next token is extracted when next() is called.

    public Tokenizer(String text) {
        buffer = text;          // save input text (string)
        next();                 // extracts the first token.
    }
    public void next() {
        if (buffer.isEmpty()) {
            currentToken = null;
            return;
        }
        buffer = buffer.trim();
        Log.d("Tokenizer", "Trimmed Buffer before processing: " + buffer);

        char firstChar = buffer.charAt(0);
        int idx = 0;

        if (firstChar == '+' || firstChar == '/' || firstChar == '<' || firstChar == '>' || firstChar == '(' || firstChar == ')') {
            currentToken = new Token(String.valueOf(firstChar), mapCharToType(firstChar));
            idx = 1; // these are single character tokens
            Log.d("Tokenizer", "Processed operator token: " + currentToken.getToken());
        } else if (Character.isDigit(firstChar) || (firstChar == '.' && buffer.length() > 1 && Character.isDigit(buffer.charAt(1)))) {
            StringBuilder number = new StringBuilder();
            boolean hasDecimalPoint = firstChar == '.';

            if (firstChar == '.') {
                number.append(firstChar);
                number.append(buffer.charAt(1));
                idx = 2; // start from the digit after the decimal point
                Log.d("Tokenizer", "Starting number with decimal: " + number.toString());
            }

            // Collect remaining parts of the number
            while (idx < buffer.length() && (Character.isDigit(buffer.charAt(idx)) || (!hasDecimalPoint && buffer.charAt(idx) == '.'))) {
                if (buffer.charAt(idx) == '.') {
                    hasDecimalPoint = true; // mark that we have found a decimal point
                    Log.d("Tokenizer", "Found decimal point in number");
                }
                number.append(buffer.charAt(idx++));
            }
            currentToken = new Token(number.toString(), Token.Type.DOUBLE);
            Log.d("Tokenizer", "Processed number token: " + number.toString() + " as DOUBLE");
        } else if (Character.isLetter(firstChar)) {
            StringBuilder identifier = new StringBuilder(String.valueOf(firstChar));
            idx = 0;
            while (idx < buffer.length() && Character.isLetter(buffer.charAt(idx))) {
                identifier.append(buffer.charAt(idx++));
            }
            currentToken = new Token(identifier.toString(), Token.Type.STR);
            Log.d("Tokenizer", "Processed identifier token: " + identifier.toString());
        } else {
            throw new Token.IllegalTokenException("Invalid token found from the expression " + firstChar + " received");
        }

        // Update the buffer to reflect the consumed characters
        buffer = buffer.substring(idx);
        Log.d("Tokenizer", "Buffer after processing: " + buffer);
        Log.d("Tokenizer", "Current token: " + currentToken.getToken() + ", Type: " + currentToken.getType());
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
