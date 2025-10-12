package bard.dao;

import bard.model.Currency;

import java.util.List;

// todo обработку ошибок сделать + тестирование + response obj
public interface CurrencyImpl {
    List<Currency> getCurrencies();

    Currency getCurrencyByCode(String code);

    Currency getCurrencyById(long id);


    void postCurrency(Currency currency);
}
