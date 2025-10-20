package bard.model;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ExchangeRateUpdateRequest {

    private BigDecimal rate;
}