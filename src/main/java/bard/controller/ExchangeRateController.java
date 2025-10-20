package bard.controller;

import bard.model.ApiResponse;
import bard.model.ExchangeRate;
import bard.model.ExchangeRateRequest;
import bard.model.ExchangeRateUpdateRequest;
import bard.service.ExchangeRateService;
import com.sun.jdi.request.InvalidRequestStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/exchangeRates")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<ExchangeRate>> getExchangeRates() {
        List<ExchangeRate> exchangeRates = exchangeRateService.getExchangeRates();

        return ApiResponse.success("Exchange rates retrieved successfully", exchangeRates);
    }

    @GetMapping("/exchangeRate/{currencyPair}")
    public ApiResponse<ExchangeRate> getExchangeRateByCode(@PathVariable String currencyPair) {
        ExchangeRate exchangeRate = exchangeRateService.getExchangeRateByCode(currencyPair);

        return ApiResponse.success("Exchange rate retrieved successfully", exchangeRate);

    }

    @GetMapping("/exchangeRate") //понял что такое себе, но как переделать. что-то с ControllerAdvice. нада разобраться
    public ResponseEntity<ApiResponse<?>> getCurrencyWithoutCode() {
        ApiResponse<?> response = ApiResponse.error(
                "ExchangeRate currencyPair is required in URL path",
                "MISSING_EXCHANGEATE_PAIR 400"
        );

        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping(value = "/exchangeRates", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> insertExchangeRate(@ModelAttribute ExchangeRateRequest exchangeRateRequest) {

        ExchangeRate createdRate = exchangeRateService.createExchangeRate(exchangeRateRequest);
        return ApiResponse.success("ExchangeRate created", null);
    }

    @PatchMapping(value = "/exchangeRate/{currencyPair}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK) //мб это
    public ApiResponse<ExchangeRate> updateExchangeRate(@PathVariable String currencyPair, @ModelAttribute ExchangeRateUpdateRequest request) {
        String baseCode = currencyPair.substring(0, 3).toUpperCase();
        String targetCode = currencyPair.substring(3).toUpperCase();

        ExchangeRate updatedRate = exchangeRateService.updateExchangeRate(baseCode, targetCode, request.getRate());
        return ApiResponse.success("Exchange rate updated successfully", updatedRate);
    }
}
