package org.ibanfirst.tradeparser.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.ibanfirst.tradeparser.exception.TradeParserException;
import org.ibanfirst.tradeparser.model.TradeConfirmation;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.ibanfirst.tradeparser.util.TradeParserUtil.extractRate;

class TradeParserUtilTest {

    @Test
    void testValidEmailContent() throws Exception {
        String emailContent = "OUR REF : 123456\n" +
                "DD: 01/04/2024\n" +
                "WE SELL: USD 1000.00\n" +
                "WE BUY: EUR 850.00\n" +
                "RATE: 1.18\n" +
                "VALUE DATE: 02/04/2024";

        TradeConfirmation tradeConfirmation = TradeParserUtil.extractFromEmailContent(emailContent);

        Assertions.assertEquals("123456", tradeConfirmation.getReference());
        Assertions.assertEquals(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2024"), tradeConfirmation.getTradeDate());
        Assertions.assertEquals("USDEUR", tradeConfirmation.getSymbol());
        Assertions.assertEquals("EUR", tradeConfirmation.getCurrency());
        Assertions.assertEquals(850.00f, tradeConfirmation.getAmount());
        Assertions.assertEquals(1000.00f, tradeConfirmation.getAmountCounterValue());

        float rate = extractRate(emailContent);
        Assertions.assertEquals(1.18f, rate);

        Assertions.assertEquals(new SimpleDateFormat("dd/MM/yyyy").parse("02/04/2024"), tradeConfirmation.getValueDate());
    }

    @Test
    void testMultipleRateMatches_selectsFirstRate() throws Exception {
        String emailContent = "OUR REF : 123456\n" +
                "DD: 01/04/2024\n" +
                "WE SELL: USD 1000.00\n" +
                "WE BUY: EUR 850.00\n" +
                "RATE: 1.18\n" +
                "RATE: 1.20\n" + // Another rate
                "VALUE DATE: 02/04/2024";

        TradeConfirmation tradeConfirmation = TradeParserUtil.extractFromEmailContent(emailContent);
        Assertions.assertEquals(1.18f, tradeConfirmation.getRate());
    }

    @Test
    void testWhitespaceHandling() throws Exception {
        String emailContent = "OUR REF : 123456\n" +
                "    DD: 01/04/2024  \n" + // Extra whitespace
                "WE SELL: USD 1000.00\n" +
                "WE BUY: EUR 850.00\n" +
                "RATE: 1.18\n" +
                "VALUE DATE: 02/04/2024";

        TradeConfirmation tradeConfirmation = TradeParserUtil.extractFromEmailContent(emailContent);
        Assertions.assertEquals(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2024"), tradeConfirmation.getTradeDate());
    }

    @Test
    void testInvalidEmailContent_throwsException() {
        String emailContent = "Invalid email content";

        Assertions.assertThrows(TradeParserException.class, () -> TradeParserUtil.extractFromEmailContent(emailContent));
    }
}
