package bard.controller;


import bard.exception.NotFoundException;
import bard.model.Currency;
import bard.model.CurrencyRequest;
import bard.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class CurrencyController {
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/currencies")
    @ResponseStatus(HttpStatus.OK)
    public List<Currency> getCurrencies() {
        return currencyService.getCurrencies();
    }

    @GetMapping({"/currency/{code}", "/currency"})
    @ResponseStatus(HttpStatus.OK)
    public Currency getCurrency(@PathVariable(required = false) String code) {
        if (currencyService.isValidCurrency(code)) {
            return currencyService.getCurrencyByCode(code);
        }
        throw new NotFoundException(code); // тут просто ругается на отсутствие return
    }

    @PostMapping(value = "/currencies")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@ModelAttribute CurrencyRequest currencyRequest) {
        Currency currency = currencyService.transformCurrencyRequestToCurrency(currencyRequest);
        currencyService.createCurrency(currency);
    }
}


