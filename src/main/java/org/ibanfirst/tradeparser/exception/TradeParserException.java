package org.ibanfirst.tradeparser.exception;

public class TradeParserException extends Exception {
    public TradeParserException(String message) {
        super(message);
    }

    public TradeParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
