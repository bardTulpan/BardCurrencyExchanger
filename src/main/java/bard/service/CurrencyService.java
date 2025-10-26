package bard.service;

import bard.dao.CurrencyRepository;
import bard.exception.InvalidRequestException;
import bard.exception.MissingException;
import bard.model.Currency;
import bard.model.CurrencyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<Currency> getCurrencies() {
        return currencyRepository.getCurrencies();
    }

    public Currency getCurrencyByCode(String code) {

        if (code == null || code.length() != 3) {
            throw new IllegalArgumentException("Currency code must be exactly 3 characters" + code);
        }

        return currencyRepository.getCurrencyByCode(code);
    }

    public void createCurrency(Currency currency) {
        currencyRepository.postCurrency(currency);
    }

    public boolean isValidCurrency(String currencyCode) {
        if (currencyCode == null || currencyCode.trim().isEmpty()) {
            throw new MissingException("Currency code can not be empty");
        }
        return true;
    }

    public Currency transformCurrencyRequestToCurrency(CurrencyRequest currencyRequest) {
        if (currencyRequest.getCode() == null || currencyRequest.getCode().trim().isEmpty()) {
            throw new InvalidRequestException("Missing required field: code");
        }
        if (currencyRequest.getName() == null || currencyRequest.getName().trim().isEmpty()) {
            throw new InvalidRequestException("Missing required field: name");
        }
        if (currencyRequest.getSign() == null || currencyRequest.getSign().trim().isEmpty()) {
            throw new InvalidRequestException("Missing required field: sign");
        }

        return new Currency(
                currencyRequest.getCode(),
                currencyRequest.getName(),
                currencyRequest.getSign()
        );
    }


}
