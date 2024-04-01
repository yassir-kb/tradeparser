package org.ibanfirst.tradeparser.service;

import org.ibanfirst.tradeparser.model.TradeConfirmation;
import org.ibanfirst.tradeparser.util.TradeParserUtil;
import org.springframework.stereotype.Service;

@Service
public class TradeParserService {

    public TradeConfirmation parseEmailContent(String emailContent) throws Exception {
        return TradeParserUtil.extractFromEmailContent(emailContent);
    }
}
