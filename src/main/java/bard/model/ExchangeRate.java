package bard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate { //todo сделать ExchangeRateResponse
    private Long id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;

    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public ExchangeRate(Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }
}
