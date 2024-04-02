package org.ibanfirst.tradeparser.exception;

/**
 * This class is used to represent exceptions that occur during the parsing of trades.
 */
public class TradeParserException extends Exception {

    /**
     * Constructs a new TradeParserException with the specified message.
     *
     * @param message the message for the exception
     */
    public TradeParserException(String message) {
        super(message);
    }

    /**
     * Constructs a new TradeParserException with the specified message and cause.
     *
     * @param message the message for the exception
     * @param cause   the cause of the exception
     */
    public TradeParserException(String message, Throwable cause) {
        super(message, cause);
    }

}