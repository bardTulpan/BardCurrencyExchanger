package bard.controller;


import bard.model.ApiResponse;
import bard.model.Currency;
import bard.service.CurrencyService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//мб сделать объект serverResponce и возвращать её
@RequestMapping("/api")
@RestController
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;


    @GetMapping("/currencies")
    public ResponseEntity<ApiResponse<List<Currency>>> getCurrencies() {
        List<Currency> currencies = currencyService.getCurrencies();

        ApiResponse<List<Currency>> response = ApiResponse.success(
                "Currencies retrieved successfully",
                currencies
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/currency/{code}")
    public ResponseEntity<ApiResponse<Currency>> getCurrency(@PathVariable String code) {

        Currency foundCurrency = currencyService.getCurrency(code);

        ApiResponse<Currency> response = ApiResponse.success(
                "Currency retrieved successfully",
                foundCurrency
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/currency")
    public ResponseEntity<ApiResponse<?>> getCurrencyWithoutCode() {
        ApiResponse<?> response = ApiResponse.error(
                "Currency code is required in URL path",
                "MISSING_CURRENCY_CODE 400"
        );

        return ResponseEntity.badRequest().body(response);
    }

    //сделать намана
    @PostMapping("/currencies")
    public ResponseEntity<ApiResponse<Void>> insertCurrency(@RequestBody @Valid Currency currency) {
        currencyService.postCurrency(currency);

        ApiResponse<Void> response = ApiResponse.success(
                "Currency created successfully",
                null
        );

        return ResponseEntity.status(200).body(response);
    }


}


