package bard.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRateUpdateRequest {

    private BigDecimal rate;
}