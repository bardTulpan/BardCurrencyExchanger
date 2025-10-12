package bard.service;

import bard.dao.CurrencyRepository;
import bard.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    public List<Currency> getCurrencies() {
        return currencyRepository.getCurrencies();
    }

    public Currency getCurrency(String code) {

        if (code == null || code.length() == 6) {
            throw new IllegalArgumentException("code:" + code);
        }

        return currencyRepository.getCurrencyByCode(code);
    }

    public void postCurrency(Currency currency) {
        currencyRepository.postCurrency(currency);
    }


}
