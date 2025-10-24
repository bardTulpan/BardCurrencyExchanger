package bard.controller;

import bard.exception.NotFoundException;
import bard.model.ExchangeRate;
import bard.model.ExchangeRateRequest;
import bard.model.ExchangeRateUpdateRequest;
import bard.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public List<ExchangeRate> getExchangeRates() {
        return exchangeRateService.getExchangeRates();
    }

    @GetMapping({"/exchangeRate/{currencyPair}", "/exchangeRate"})
    public ExchangeRate getExchangeRateByCode(@PathVariable(required = false) String currencyPair) {
        if (exchangeRateService.isValidCurrencyPair(currencyPair)) {
            return exchangeRateService.getExchangeRateByCode(currencyPair);
        }
        throw new NotFoundException(currencyPair); // тут просто ругается на отсутствие return
    }


    @PostMapping(value = "/exchangeRates")
    @ResponseStatus(HttpStatus.CREATED)
    public void insertExchangeRate(@ModelAttribute ExchangeRateRequest exchangeRateRequest) {
        exchangeRateService.isValidExchangeRateRequest(exchangeRateRequest);
        exchangeRateService.createExchangeRate(exchangeRateRequest);
    }

    @PatchMapping(value = "/exchangeRate/{currencyPair}")
    @ResponseStatus(HttpStatus.OK)
    public ExchangeRate updateExchangeRate(@PathVariable String currencyPair, @ModelAttribute ExchangeRateUpdateRequest request) {
        String baseCode = currencyPair.substring(0, 3).toUpperCase();
        String targetCode = currencyPair.substring(3).toUpperCase();

        return exchangeRateService.updateExchangeRate(baseCode, targetCode, request.getRate());

    }
}
