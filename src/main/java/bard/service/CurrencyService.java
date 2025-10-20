package bard.service;

import bard.dao.CurrencyRepository;
import bard.model.Currency;
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


}
