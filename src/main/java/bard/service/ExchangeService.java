package bard.service;

import bard.model.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ExchangeService {

    public final ExchangeRateService exchangeRateService; // МБ так лучше, чем все в exchangeRateServec'e писать

    @Autowired
    public ExchangeService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public ExchangeRate exchange(String fromCurrencyCode, String toCurrencyCode, BigDecimal amount) {
        if (exchangeRateService.isExistsExchangeRate(fromCurrencyCode, toCurrencyCode)) {
            ExchangeRate exchangeRate = exchangeRateService.getExchangeRateByCode(fromCurrencyCode + toCurrencyCode);
            return createConversionResult(exchangeRate, amount, false);
        } else if (exchangeRateService.isExistsExchangeRate(toCurrencyCode, fromCurrencyCode)) {
            ExchangeRate reverseRate = exchangeRateService.getExchangeRateByCode(toCurrencyCode + fromCurrencyCode);
            return createConversionResult(reverseRate, amount, true);
        } else {
            return calculateCrossRateViaUSD(fromCurrencyCode, toCurrencyCode, amount);
        }
    }

    private ExchangeRate createConversionResult(ExchangeRate exchangeRate, BigDecimal amount, boolean isReverse) {
        ExchangeRate result = new ExchangeRate();
        result.setBaseCurrency(exchangeRate.getBaseCurrency());
        result.setTargetCurrency(exchangeRate.getTargetCurrency());
        result.setRate(exchangeRate.getRate());
        result.setAmount(amount);

        if (isReverse) {
            BigDecimal reverseRate = BigDecimal.ONE.divide(exchangeRate.getRate(), 6, RoundingMode.HALF_UP);
            BigDecimal convertedAmount = amount.multiply(reverseRate);
            result.setConvertedAmount(convertedAmount);

            result.setBaseCurrency(exchangeRate.getTargetCurrency());
            result.setTargetCurrency(exchangeRate.getBaseCurrency());
            result.setRate(reverseRate);
        } else {
            BigDecimal convertedAmount = amount.multiply(exchangeRate.getRate());
            result.setConvertedAmount(convertedAmount);
        }
        return result;
    }

    private ExchangeRate calculateCrossRateViaUSD(String fromCurrencyCode, String toCurrencyCode, BigDecimal amount) {
        // Получаем курсы USD → A и USD → B
        ExchangeRate usdToFrom = exchangeRateService.getExchangeRateByCode("EUR" + fromCurrencyCode);
        ExchangeRate usdToTo = exchangeRateService.getExchangeRateByCode("EUR" + toCurrencyCode);

        // Вычисляем кросс-курс: A → B = (USD → B) / (USD → A)
        BigDecimal crossRate = usdToTo.getRate().divide(usdToFrom.getRate(), 6, RoundingMode.HALF_UP);
        BigDecimal convertedAmount = amount.multiply(crossRate);

        ExchangeRate result = new ExchangeRate();
        result.setBaseCurrency(usdToFrom.getTargetCurrency()); // Валюта A
        result.setTargetCurrency(usdToTo.getTargetCurrency()); // Валюта B
        result.setRate(crossRate);
        result.setAmount(amount);
        result.setConvertedAmount(convertedAmount);

        return result;
    }

}
