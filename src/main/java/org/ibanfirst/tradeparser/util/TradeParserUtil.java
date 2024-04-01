package org.ibanfirst.tradeparser.util;

import org.ibanfirst.tradeparser.exception.TradeParserException;
import org.ibanfirst.tradeparser.model.TradeConfirmation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TradeParserUtil {

    public static TradeConfirmation extractFromEmailContent(String emailContent) throws Exception {
        try {
            String reference = extractField("OUR REF : (\\d+)", emailContent);
            Date tradeDate = extractDate("DD: (\\d{2}/\\d{2}/\\d{4})", emailContent);
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

    private static String extractField(String pattern, String emailContent) throws ParseException {
        Matcher matcher = Pattern.compile(pattern).matcher(emailContent);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new ParseException("Missing field: " + pattern, 0);
        }
    }

    private static Date extractDate(String pattern, String emailContent) throws ParseException {
        String dateStr = extractField(pattern, emailContent);
        SimpleDateFormat sdf = new SimpleDateFormat("git init");
        return sdf.parse(dateStr);
    }

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

    private static final String RATE_PATTERN = "RATE: ([\\d,\\.]+)";

    static float extractRate(String emailContent) {
        Pattern ratePattern = Pattern.compile(RATE_PATTERN);
        Matcher matcher = ratePattern.matcher(emailContent);
        if (matcher.find()) {
            return Float.parseFloat(matcher.group(1));
        }
        return 0;
    }

}

