package org.ibanfirst.tradeparser.util;

import org.ibanfirst.tradeparser.exception.TradeParserException;
import org.ibanfirst.tradeparser.model.TradeConfirmation;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for parsing trade confirmation data from email content.
 */
public class TradeParserUtil {

    /**
     * Regular expression pattern for extracting the rate from the email content.
     */
    private static final String RATE_PATTERN = "RATE: ([\\d,\\.]+)";

    /**
     * Extracts trade confirmation data from the email content.
     *
     * @param emailContent the email content
     * @return the trade confirmation
     * @throws TradeParserException if an error occurs while parsing the trade confirmation data
     */
    public static TradeConfirmation extractFromEmailContent(String emailContent) throws TradeParserException {
        try {
            String reference = extractField("OUR REF : (\\d+)", emailContent);
            Date tradeDate = extractDate("FOREX Deal - (\\d{2}/\\d{2}/\\d{4})", emailContent);
            String[] sellData = extractCurrencyAmount("WE SELL: (\\w+) ([\\d,\\.]+)", emailContent);
            String[] buyData = extractCurrencyAmount("WE BUY: (\\w+) ([\\d,\\.]+)", emailContent);
            float rate = extractRate(emailContent);
            Date valueDate = extractDate("VALUE DATE: (\\d{2}/\\d{2}/\\d{4})", emailContent);
            String symbol = sellData[0] + buyData[0];
            String currency = buyData[0];
            float amount = Float.parseFloat(buyData[1].replaceAll(",", ""));
            float amountCounterValue = Float.parseFloat(sellData[1].replaceAll(",", ""));

            return new TradeConfirmation(reference, tradeDate, symbol, currency, amount, amountCounterValue, rate, valueDate);
        } catch (ParseException | NullPointerException e) {
            throw new TradeParserException("Error parsing trade confirmation from email", e);
        }
    }

    /**
     * Extracts a field from the email content using the given regular expression pattern.
     *
     * @param pattern      the regular expression pattern
     * @param emailContent the email content
     * @return the field value
     * @throws ParseException if the field cannot be extracted
     */
    private static String extractField(String pattern, String emailContent) throws ParseException {
        Matcher matcher = Pattern.compile(pattern).matcher(emailContent);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new ParseException("Missing field: " + pattern, 0);
        }
    }

    /**
     * Extracts a date from the email content using the given date format pattern.
     *
     * @param pattern      the date format pattern
     * @param emailContent the email content
     * @return the date value
     * @throws ParseException if the date cannot be extracted
     */
    private static Date extractDate(String pattern, String emailContent) throws ParseException {
        String dateStr = extractField(pattern, emailContent);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sdf.parse(dateStr);
    }

    /**
     * Extracts the currency and amount from the email content using the given regular expression pattern.
     *
     * @param pattern      the regular expression pattern
     * @param emailContent the email content
     * @return an array containing the currency and amount
     */
    private static String[] extractCurrencyAmount(String pattern, String emailContent) {
        Pattern currencyAmountPattern = Pattern.compile(pattern);
        Matcher matcher = currencyAmountPattern.matcher(emailContent);
        String[] result = new String[2];
        if (matcher.find()) {
            result[0] = matcher.group(1);
            result[1] = matcher.group(2);
        }
        return result;
    }

    /**
     * Extracts the rate from the email content using the RATE_PATTERN regular expression pattern.
     *
     * @param emailContent the email content
     * @return the rate
     */
    static float extractRate(String emailContent) {
        Pattern ratePattern = Pattern.compile(RATE_PATTERN);
        Matcher matcher = ratePattern.matcher(emailContent);
        if (matcher.find()) {
            return Float.parseFloat(matcher.group(1));
        }
        return 0;
    }

}