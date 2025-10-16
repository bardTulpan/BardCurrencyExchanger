package bard.controller;


import bard.model.ApiResponse;
import bard.model.Currency;
import bard.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//мб сделать объект serverResponce и возвращать её
@RequestMapping("/api")
@RestController
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;


    @GetMapping("/currencies")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<Currency>> getCurrencies() {
        List<Currency> currencies = currencyService.getCurrencies();

        return ApiResponse.success("Currencies retrieved successfully", currencies);
    }

    @GetMapping("/currency/{code}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Currency> getCurrency(@PathVariable String code) {
        Currency foundCurrency = currencyService.getCurrencyByCode(code);

        return ApiResponse.success("Currency retrieved successfully", foundCurrency);
    }

    @GetMapping("/currency") //понял, как это переделать
    public ResponseEntity<ApiResponse<?>> getCurrencyWithoutCode() {
        ApiResponse<?> response = ApiResponse.error(
                "Currency code is required in URL path",
                "MISSING_CURRENCY_CODE 400"
        );
        return ResponseEntity.badRequest().body(response);
    }

    //сделать намана
    @PostMapping(value = "/currencies", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> createCurrency(@ModelAttribute @Valid Currency currency) {
        currencyService.createCurrency(currency);

        return ApiResponse.success("Currency created", null);

    }


}


