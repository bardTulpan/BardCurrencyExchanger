package bard.model;

import lombok.Data;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ExchangeRateUpdateRequest {

    @NotNull(message = "Rate is required")
    @DecimalMin(value = "0.000001", message = "Rate must be positive")
    private BigDecimal rate;
}