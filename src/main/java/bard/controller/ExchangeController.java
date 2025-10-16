package bard.controller;

import bard.model.ApiResponse;
import bard.model.ExchangeRate;
import bard.model.ExchangeRequest;
import bard.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ExchangeController {
    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/exchange")
    public ResponseEntity<ApiResponse<ExchangeRate>> convertCurrency(
            @RequestBody @Valid ExchangeRequest request) {

        ExchangeRate result = exchangeService.exchange(
                request.getBaseCurrencyCode(),
                request.getTargetCurrencyCode(),
                request.getAmount()
        );

        ApiResponse<ExchangeRate> response = ApiResponse.success(
                "Currency converted successfully", result
        );
        return ResponseEntity.ok(response);
    }
}

