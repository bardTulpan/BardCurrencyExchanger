package bard.controller;

import bard.model.ApiResponse;
import bard.model.ExchangeRate;
import bard.model.ExchangeRequest;
import bard.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api") //todo тут переписать в modelattribyte и другие модели возвращать responce и можно запушить
public class ExchangeController {
    private final ExchangeService exchangeService;

    @Autowired
    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping(value = "/exchange", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ExchangeRate> convertCurrency(@ModelAttribute ExchangeRequest request) {

        ExchangeRate result = exchangeService.exchange(
                request.getBaseCurrencyCode(),
                request.getTargetCurrencyCode(),
                request.getAmount()
        );
        return ApiResponse.success("Currency converted successfully", result);
    }
}

