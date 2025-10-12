package bard.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class ExchangeRequest {
    @NotBlank(message = "Base currency code is required")
    private String baseCurrencyCode;
    @NotBlank(message = "Target currency code is required")
    private String targetCurrencyCode;
    @NotBlank(message = "Amount code is required")
    private BigDecimal amount;
}
