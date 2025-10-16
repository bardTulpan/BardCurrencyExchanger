package bard.model;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ExchangeRequest {
    @NotBlank(message = "Base currency code is required")
    private String baseCurrencyCode;
    @NotBlank(message = "Target currency code is required")
    private String targetCurrencyCode;
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;
}
