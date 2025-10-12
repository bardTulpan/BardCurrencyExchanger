package bard.dao;

import bard.exception.DatabaseException;
import bard.exception.currency.SameCurrencyException;
import bard.exception.exchangeRate.ExchangeRateAlreadyExistsException;
import bard.exception.exchangeRate.ExchangeRateNotFoundException;
import bard.exception.exchangeRate.InvalidExchangeRateException;
import bard.model.Currency;
import bard.model.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ExchangeRateRepository implements ExchangeRateImpl {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<ExchangeRate> exchangeRateRowMapper = (rs, rowNum) -> {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setId(rs.getLong("er_id"));
        exchangeRate.setRate(rs.getBigDecimal("er_rate"));

        // Базовая валюта - используем точные названия из CREATE TABLE
        Currency baseCurrency = new Currency();
        baseCurrency.setId(rs.getLong("bc_id"));
        baseCurrency.setCode(rs.getString("bc_code"));
        baseCurrency.setFullName(rs.getString("bc_fullname")); // FullName → FullName
        baseCurrency.setSign(rs.getString("bc_sign"));
        exchangeRate.setBaseCurrency(baseCurrency);

        // Целевая валюта
        Currency targetCurrency = new Currency();
        targetCurrency.setId(rs.getLong("tc_id"));
        targetCurrency.setCode(rs.getString("tc_code"));
        targetCurrency.setFullName(rs.getString("tc_fullname")); // FullName → FullName
        targetCurrency.setSign(rs.getString("tc_sign"));
        exchangeRate.setTargetCurrency(targetCurrency);

        return exchangeRate;
    };

    @Override
    public List<ExchangeRate> getExchangeRates() {
        try {
            String sql = """
                    SELECT 
                        er.ID as er_id, 
                        er.Rate as er_rate,
                        bc.ID as bc_id, bc.Code as bc_code, bc.FullName as bc_fullname, bc.Sign as bc_sign,
                        tc.ID as tc_id, tc.Code as tc_code, tc.FullName as tc_fullname, tc.Sign as tc_sign
                    FROM ExchangeRates er
                    JOIN Currencies bc ON er.BaseCurrencyId = bc.ID
                    JOIN Currencies tc ON er.TargetCurrencyId = tc.ID
                    """;
            return jdbcTemplate.query(sql, exchangeRateRowMapper);
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to fetch exchange rates: " + e.getMessage());
        }
    }

    @Override
    public ExchangeRate getExchangeRateByCodes(String baseCode, String targetCode) {
        try {
            String sql = """
                    SELECT 
                        er.ID as er_id, 
                        er.Rate as er_rate,
                        bc.ID as bc_id, bc.Code as bc_code, bc.FullName as bc_fullname, bc.Sign as bc_sign,
                        tc.ID as tc_id, tc.Code as tc_code, tc.FullName as tc_fullname, tc.Sign as tc_sign
                    FROM ExchangeRates er
                    JOIN Currencies bc ON er.BaseCurrencyId = bc.ID
                    JOIN Currencies tc ON er.TargetCurrencyId = tc.ID
                    WHERE bc.Code = ? AND tc.Code = ?
                    """;
            return jdbcTemplate.queryForObject(sql, exchangeRateRowMapper, baseCode, targetCode);
        } catch (EmptyResultDataAccessException e) {
            throw new bard.exception.exchangeRate.ExchangeRateNotFoundException(baseCode, targetCode);
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to fetch exchange rate: " + e.getMessage());
        }
    }

//    @Override
//    public ExchangeRate postExchangeRate(ExchangeRate exchangeRate) {
//        try {
//            // Валидация
//            if (exchangeRate.getBaseCurrency().getCode().equals(exchangeRate.getTargetCurrency().getCode())) {
//                throw new SameCurrencyException(exchangeRate.getBaseCurrency().getCode());
//            }
//
//            if (exchangeRate.getRate().compareTo(java.math.BigDecimal.ZERO) <= 0) {
//                throw new InvalidExchangeRateException(String.valueOf(exchangeRate.getRate()));
//            }
//
//            // Проверка на существование
//            String checkSql = """
//                    SELECT COUNT(*) FROM ExchangeRates er
//                    JOIN Currencies bc ON er.BaseCurrencyId = bc.ID
//                    JOIN Currencies tc ON er.TargetCurrencyId = tc.ID
//                    WHERE bc.Code = ? AND tc.Code = ?
//                    """;
//            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class,
//                    exchangeRate.getBaseCurrency().getCode(),
//                    exchangeRate.getTargetCurrency().getCode());
//
//            if (count != null && count > 0) {
//                throw new ExchangeRateAlreadyExistsException(
//                        exchangeRate.getBaseCurrency().getCode(),
//                        exchangeRate.getTargetCurrency().getCode()
//                );
//            }
//
//            String sql = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
//            jdbcTemplate.update(sql,
//                    exchangeRate.getBaseCurrency().getId(),
//                    exchangeRate.getTargetCurrency().getId(),
//                    exchangeRate.getRate()
//            );
//
//        } catch (DataAccessException e) {
//            throw new DatabaseException("Failed to insert exchange rate: " + e.getMessage());
//        }
//    }
    public ExchangeRate postExchangeRate(ExchangeRate exchangeRate) {
        try {
            // Валидация
            if (exchangeRate.getBaseCurrency().getCode().equals(exchangeRate.getTargetCurrency().getCode())) {
                throw new SameCurrencyException(exchangeRate.getBaseCurrency().getCode());
            }

            if (exchangeRate.getRate().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new InvalidExchangeRateException(String.valueOf(exchangeRate.getRate()));
            }

            // Проверка на существование
            String checkSql = """
                    SELECT COUNT(*) FROM ExchangeRates er
                    JOIN Currencies bc ON er.BaseCurrencyId = bc.ID
                    JOIN Currencies tc ON er.TargetCurrencyId = tc.ID
                    WHERE bc.Code = ? AND tc.Code = ?
                    """;
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class,
                    exchangeRate.getBaseCurrency().getCode(),
                    exchangeRate.getTargetCurrency().getCode());

            if (count != null && count > 0) {
                throw new ExchangeRateAlreadyExistsException(
                        exchangeRate.getBaseCurrency().getCode(),
                        exchangeRate.getTargetCurrency().getCode()
                );
            }

            // Вставка и получение ID
            String sql = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
                ps.setLong(1, exchangeRate.getBaseCurrency().getId());
                ps.setLong(2, exchangeRate.getTargetCurrency().getId());
                ps.setBigDecimal(3, exchangeRate.getRate());
                return ps;
            }, keyHolder);

            // Устанавливаем ID в объект
            Long id = keyHolder.getKey().longValue();
            exchangeRate.setId(id);

            return exchangeRate;

        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to insert exchange rate: " + e.getMessage());
        }
    }

    public ExchangeRate updateExchangeRate(String baseCode, String targetCode, BigDecimal newRate) {
        try {
            String sql = """
                UPDATE ExchangeRates 
                SET Rate = ? 
                WHERE BaseCurrencyId = (SELECT ID FROM Currencies WHERE Code = ?)
                AND TargetCurrencyId = (SELECT ID FROM Currencies WHERE Code = ?)
                """;

            int affected = jdbcTemplate.update(sql, newRate, baseCode, targetCode);

            if (affected == 0) {
                throw new ExchangeRateNotFoundException(baseCode, targetCode);
            }

            // Возвращаем обновленный курс
            return getExchangeRateByCodes(baseCode, targetCode);

        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to update exchange rate: " + e.getMessage());
        }
    }
}

