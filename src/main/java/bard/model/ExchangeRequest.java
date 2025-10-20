package bard.model;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ExchangeRequest {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal amount;
}
