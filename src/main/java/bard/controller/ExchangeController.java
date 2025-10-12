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
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExchangeController {
    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/exchange")
    public ResponseEntity<ApiResponse<ExchangeRate>> exchange(@RequestBody @Valid ExchangeRequest request) {     //todo mb изменить имя метода

    }
}
