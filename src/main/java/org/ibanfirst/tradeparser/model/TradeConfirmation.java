package org.ibanfirst.tradeparser.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * TradeConfirmation represents a trade confirmation message.
 */
@Data
@AllArgsConstructor
public class TradeConfirmation {

    /**
     * The reference of the trade.
     */
    @JsonProperty("reference")
    private String reference;

    /**
     * The trade date.
     */
    @JsonProperty("tradeDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tradeDate;

    /**
     * The symbol of the asset.
     */
    @JsonProperty("symbol")
    private String symbol;

    /**
     * The currency of the asset.
     */
    @JsonProperty("currency")
    private String currency;

    /**
     * The amount of the asset.
     */
    @JsonProperty("amount")
    private float amount;

    /**
     * The amount counter value of the asset.
     */
    @JsonProperty("amountCounterValue")
    private float amountCounterValue;

    /**
     * The rate of the asset.
     */
    @JsonProperty("rate")
    private float rate;

    /**
     * The value date.
     */
    @JsonProperty("valueDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date valueDate;

    /**
     * Returns a JSON representation of the TradeConfirmation object.
     *
     * @return a JSON representation of the TradeConfirmation object
     * @throws JsonProcessingException if an error occurs during JSON processing
     */
    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }
}