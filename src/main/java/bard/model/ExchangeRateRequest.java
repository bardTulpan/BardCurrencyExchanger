package bard.model;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class ExchangeRateRequest {

    @NotBlank(message = "Base currency code is required")
    @Size(min = 3, max = 3, message = "Base currency code must be exactly 3 characters")
    private String baseCurrencyCode;

    @NotBlank(message = "Target currency code is required")
    @Size(min = 3, max = 3, message = "Target currency code must be exactly 3 characters")
    private String targetCurrencyCode;

    @NotNull(message = "Rate is required")
    @DecimalMin(value = "0.000001", message = "Rate must be positive")
    private BigDecimal rate;
}