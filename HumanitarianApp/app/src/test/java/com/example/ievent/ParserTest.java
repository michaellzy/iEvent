package com.example.ievent;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.example.ievent.tokenparser.Exp;
import com.example.ievent.tokenparser.Parser;
import com.example.ievent.tokenparser.Tokenizer;

public class ParserTest {

    @Test
    public void testSimpleCondition() {
        String input = "price > 100";
        Tokenizer tokenizer = new Tokenizer(input);
        Parser parser = new Parser(tokenizer);
        Exp result = parser.parse();

        assertEquals("(price > 100.0)", result.toString());
    }

    @Test
    public void testComplexExpression() {
        String input = "(price > 100 + price < 300) / price > 500";
        Tokenizer tokenizer = new Tokenizer(input);
        Parser parser = new Parser(tokenizer);
        Exp result = parser.parse();

        assertEquals("(((price > 100.0) AND (price < 300.0)) OR (price > 500.0))", result.toString());
    }

    @Test(expected = Parser.IllegalProductionException.class)
    public void testInvalidInput() {
        String input = "price >> 100";
        Tokenizer tokenizer = new Tokenizer(input);
        Parser parser = new Parser(tokenizer);
        parser.parse(); // Should throw an exception
    }
}