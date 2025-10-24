package bard.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRequest {
    private String from;
    private String to;
    private BigDecimal amount;
}
