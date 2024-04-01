package org.ibanfirst.tradeparser.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TradeConfirmation {

    @JsonProperty("reference")
    private String reference;

    @JsonProperty("tradeDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tradeDate;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("amount")
    private float amount;

    @JsonProperty("amountCounterValue")
    private float amountCounterValue;

    @JsonProperty("rate")
    private float rate;

    @JsonProperty("valueDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date valueDate;

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
