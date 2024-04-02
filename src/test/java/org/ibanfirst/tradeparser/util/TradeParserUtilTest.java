package org.ibanfirst.tradeparser.util;

import org.junit.jupiter.api.Test;
import org.ibanfirst.tradeparser.exception.TradeParserException;
import org.ibanfirst.tradeparser.model.TradeConfirmation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link TradeParserUtil} class.
 */
class TradeParserUtilTest {

    /**
     * Tests that a valid email content can be parsed into a {@link TradeConfirmation} object.
     *
     * @throws ParseException if the trade date or value date cannot be parsed
     */
    @Test
    void testValidEmailContent() throws ParseException, TradeParserException {
        String emailContent = "OUR REF : 123456\n" +
                "FOREX Deal - 01/04/2024\n" +
                "WE SELL: USD 1000.00\n" +
                "WE BUY: EUR 850.00\n" +
                "RATE: 1.18\n" +
                "VALUE DATE: 02/04/2024";

        TimeZone.setDefault(TimeZone.getTimeZone("UTC")); // Set default timezone to UTC

        TradeConfirmation tradeConfirmation = TradeParserUtil.extractFromEmailContent(emailContent);

        assertEquals("123456", tradeConfirmation.getReference());
        assertEquals(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2024"), tradeConfirmation.getTradeDate());
        assertEquals("USDEUR", tradeConfirmation.getSymbol());
        assertEquals("EUR", tradeConfirmation.getCurrency());
        assertEquals(850.00f, tradeConfirmation.getAmount());
        assertEquals(1000.00f, tradeConfirmation.getAmountCounterValue());

        float rate = TradeParserUtil.extractRate(emailContent); // Ensure rate extraction works independently
        assertEquals(1.18f, rate);

        assertEquals(new SimpleDateFormat("dd/MM/yyyy").parse("02/04/2024"), tradeConfirmation.getValueDate());
    }

    /**
     * Tests that multiple rates in the email content are handled correctly, and the first rate is selected.
     *
     * @throws ParseException if the trade date or value date cannot be parsed
     */
    @Test
    void testMultipleRateMatches_selectsFirstRate() throws TradeParserException {
        String emailContent = "OUR REF : 123456\n" +
                "FOREX Deal - 01/04/2024\n" +
                "WE SELL: USD 1000.00\n" +
                "WE BUY: EUR 850.00\n" +
                "RATE: 1.18\n" +
                "RATE: 1.20\n" + // Another rate
                "VALUE DATE: 02/04/2024";

        TimeZone.setDefault(TimeZone.getTimeZone("UTC")); // Set default timezone to UTC

        TradeConfirmation tradeConfirmation = TradeParserUtil.extractFromEmailContent(emailContent);
        assertEquals(1.18f, tradeConfirmation.getRate());
    }

    /**
     * Tests that whitespace in the email content is handled correctly.
     *
     * @throws ParseException if the trade date cannot be parsed
     */
    @Test
    void testWhitespaceHandling() throws ParseException, TradeParserException {
        String emailContent = "OUR REF : 123456\n" +
                "    FOREX Deal - 01/04/2024\n" + // Extra whitespace
                "WE SELL: USD 1000.00\n" +
                "WE BUY: EUR 850.00\n" +
                "RATE: 1.18\n" +
                "VALUE DATE: 02/04/2024";

        TimeZone.setDefault(TimeZone.getTimeZone("UTC")); // Set default timezone to UTC

        TradeConfirmation tradeConfirmation = TradeParserUtil.extractFromEmailContent(emailContent);
        assertEquals(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2024"), tradeConfirmation.getTradeDate());
    }

    /**
     * Tests that an invalid email content throws a {@link TradeParserException}.
     */
    @Test
    void testInvalidEmailContent_throwsException() {
        String emailContent = "Invalid email content";

        assertThrows(TradeParserException.class, () -> TradeParserUtil.extractFromEmailContent(emailContent));
    }

    /**
     * Tests that the rate can be extracted from the email content correctly.
     */
    @Test
    void testExtractRate() {
        // Test when rate is present in the email content
        String emailContentWithRate = "RATE: 1.18\n";
        float rate = TradeParserUtil.extractRate(emailContentWithRate);
        assertEquals(1.18f, rate);

        // Test when rate is not present in the email content
        String emailContentWithoutRate = "No rate information\n";
        rate = TradeParserUtil.extractRate(emailContentWithoutRate);
        assertEquals(0f, rate);
    }
}