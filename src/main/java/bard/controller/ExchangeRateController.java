package bard.controller;

import bard.model.ApiResponse;
import bard.model.ExchangeRate;
import bard.model.ExchangeRateRequest;
import bard.model.ExchangeRateUpdateRequest;
import bard.service.ExchangeRateService;
import com.sun.jdi.request.InvalidRequestStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping("/exchangeRates")
    public ResponseEntity<ApiResponse<List<ExchangeRate>>> getExchangeRates() {
        List<ExchangeRate> exchangeRates = exchangeRateService.getExchangeRates();

        ApiResponse<List<ExchangeRate>> response = ApiResponse.success(
                "Exchange rates retrieved successfully",
                exchangeRates
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/exchangeRate/{currencyPair}")
    public ResponseEntity<ApiResponse<ExchangeRate>> getExchangeRateByCode(@PathVariable String currencyPair) {
        ExchangeRate exchangeRate = exchangeRateService.getExchangeRateByCode(currencyPair);

        ApiResponse<ExchangeRate> response = ApiResponse.success(
                "Exchange rate retrieved successfully",
                exchangeRate
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/exchangeRate")
    public ResponseEntity<ApiResponse<?>> getCurrencyWithoutCode() {
        ApiResponse<?> response = ApiResponse.error(
                "ExchangeRate currencyPair is required in URL path",
                "MISSING_EXCHANGEATE_PAIR 400"
        );

        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/exchangeRates")
    public ResponseEntity<ApiResponse<ExchangeRate>> insertExchangeRate(@RequestBody @Valid ExchangeRateRequest exchangeRateRequest) {

        ExchangeRate createdRate = exchangeRateService.postExchangeRate(exchangeRateRequest);


        ApiResponse<ExchangeRate> response = ApiResponse.success(
                "Exchange rate created successfully",
                createdRate
        );

        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/exchangeRate/{currencyPair}")
    public ResponseEntity<ApiResponse<ExchangeRate>> updateExchangeRate(@PathVariable String currencyPair, @RequestBody @Valid ExchangeRateUpdateRequest request) {
        if (currencyPair == null || currencyPair.length() != 6) {
            throw new InvalidRequestStateException("Currency pair must be exactly 6 characters");
        }

        String baseCode = currencyPair.substring(0, 3).toUpperCase();
        String targetCode = currencyPair.substring(3).toUpperCase();

        ExchangeRate updatedRate = exchangeRateService.updateExchangeRate(baseCode, targetCode, request.getRate());

        ApiResponse<ExchangeRate> response = ApiResponse.success(
                "Exchange rate updated successfully",
                updatedRate
        );
        return ResponseEntity.ok(response);
    }
}
