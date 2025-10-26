//package bard.dao;
//
//import bard.exception.ConflictException;
//import bard.exception.DatabaseException;
//import bard.exception.exchangeRate.ExchangeRateAlreadyExistsException;
//import bard.exception.exchangeRate.ExchangeRateNotFoundException;
//import bard.exception.exchangeRate.InvalidExchangeRateException;
//import bard.model.Currency;
//import bard.model.ExchangeRate;
//import bard.service.CurrencyService;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessException;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import org.springframework.jdbc.support.KeyHolder;
//import org.springframework.stereotype.Repository;
//
//import java.math.BigDecimal;
//import java.sql.PreparedStatement;
//import java.util.List;
//import java.util.logging.Logger;
//
//@Repository
//public class ExchangeRateRepository {
//
//    private final RowMapper<ExchangeRate> exchangeRateRowMapper = (rs, rowNum) -> {
//        ExchangeRate exchangeRate = new ExchangeRate();
//        exchangeRate.setId(rs.getLong("er_id"));
//        exchangeRate.setRate(rs.getBigDecimal("er_rate"));
//
//        // Базовая валюта - используем точные названия из CREATE TABLE
//        Currency baseCurrency = new Currency();
//        baseCurrency.setId(rs.getLong("bc_id"));
//        baseCurrency.setCode(rs.getString("bc_code"));
//        baseCurrency.setFullName(rs.getString("bc_fullname")); // FullName → FullName
//        baseCurrency.setSign(rs.getString("bc_sign"));
//        exchangeRate.setBaseCurrency(baseCurrency);
//
//        // Целевая валюта
//        Currency targetCurrency = new Currency();
//        targetCurrency.setId(rs.getLong("tc_id"));
//        targetCurrency.setCode(rs.getString("tc_code"));
//        targetCurrency.setFullName(rs.getString("tc_fullname")); // FullName → FullName
//        targetCurrency.setSign(rs.getString("tc_sign"));
//        exchangeRate.setTargetCurrency(targetCurrency);
//
//        return exchangeRate;
//    };
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    public List<ExchangeRate> getExchangeRates() {
//        try {
//            String sql = """
//                    SELECT
//                        er.id as er_id,
//                        er.rate as er_rate,
//                        bc.id as bc_id,
//                        bc.code as bc_code,
//                        bc.full_name as bc_fullname,
//                        bc.sign as bc_sign,
//                        tc.id as tc_id,
//                        tc.code as tc_code,
//                        tc.full_name as tc_fullname,
//                        tc.sign as tc_sign
//                    FROM exchange_rates er
//                    JOIN currencies bc ON er.base_currency_id = bc.id
//                    JOIN currencies tc ON er.target_currency_id = tc.id
//                    """;
//            return jdbcTemplate.query(sql, exchangeRateRowMapper);
//        } catch (DataAccessException e) {
//            throw new DatabaseException("Failed to fetch exchange rates: " + e.getMessage());
//        }
//    }
//
//    public ExchangeRate getExchangeRateByCodes(String baseCode, String targetCode) {
//        try {
//            String sql = """
//                    SELECT
//                        er.id as er_id,
//                        er.rate as er_rate,
//                        bc.id as bc_id,
//                        bc.code as bc_code,
//                        bc.full_name as bc_fullname,
//                        bc.sign as bc_sign,
//                        tc.id as tc_id,
//                        tc.code as tc_code,
//                        tc.full_name as tc_fullname,
//                        tc.sign as tc_sign
//                    FROM ExchangeRates er
//                    JOIN Currencies bc ON er.BaseCurrencyId = bc.ID
//                    JOIN Currencies tc ON er.TargetCurrencyId = tc.ID
//                    WHERE bc.Code = ? AND tc.Code = ?
//                    """;
//            return jdbcTemplate.queryForObject(sql, exchangeRateRowMapper, baseCode, targetCode);
//        } catch (EmptyResultDataAccessException e) {
//            throw new bard.exception.exchangeRate.ExchangeRateNotFoundException(baseCode, targetCode);
//        } catch (DataAccessException e) {
//            throw new DatabaseException("Failed to fetch exchange rate: " + e.getMessage());
//        }
//    }
//
//    public ExchangeRate postExchangeRate(ExchangeRate exchangeRate) {
//        try {
//            // Валидация
//            if (exchangeRate.getBaseCurrency().getCode().equals(exchangeRate.getTargetCurrency().getCode())) {
//                throw new ConflictException(exchangeRate.getBaseCurrency().getCode());
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
//            // Вставка и получение ID
//            String sql = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
//            KeyHolder keyHolder = new GeneratedKeyHolder();
//
//            jdbcTemplate.update(connection -> {
//                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
//                ps.setLong(1, exchangeRate.getBaseCurrency().getId());
//                ps.setLong(2, exchangeRate.getTargetCurrency().getId());
//                ps.setBigDecimal(3, exchangeRate.getRate());
//                return ps;
//            }, keyHolder);
//
//            // Устанавливаем ID в объект
//            Long id = keyHolder.getKey().longValue();
//            exchangeRate.setId(id);
//
//            return exchangeRate;
//
//        } catch (DataAccessException e) {
//            throw new DatabaseException("Failed to insert exchange rate: " + e.getMessage());
//        }
//    }
//
//    public ExchangeRate updateExchangeRate(String baseCode, String targetCode, BigDecimal newRate) {
//        try {
//            String sql = """
//                    UPDATE ExchangeRates
//                    SET Rate = ?
//                    WHERE BaseCurrencyId = (SELECT ID FROM Currencies WHERE Code = ?)
//                    AND TargetCurrencyId = (SELECT ID FROM Currencies WHERE Code = ?)
//                    """;
//
//            int affected = jdbcTemplate.update(sql, newRate, baseCode, targetCode);
//
//            if (affected == 0) {
//                throw new ExchangeRateNotFoundException(baseCode, targetCode);
//            }
//
//            // Возвращаем обновленный курс
//            return getExchangeRateByCodes(baseCode, targetCode);
//
//        } catch (DataAccessException e) {
//            throw new DatabaseException("Failed to update exchange rate: " + e.getMessage());
//        }
//    }
//
//    public boolean isExchangeRateExists(String baseCurrencyCode, String targetCurrencyCode) {
//        try {
//            String checkSql = """
//                    SELECT COUNT(*) FROM ExchangeRates er
//                    JOIN Currencies bc ON er.BaseCurrencyId = bc.ID
//                    JOIN Currencies tc ON er.TargetCurrencyId = tc.ID
//                    WHERE bc.Code = ? AND tc.Code = ?
//                    """;
//            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class,
//                    baseCurrencyCode,
//                    targetCurrencyCode);
//
//            return (count != null && count > 0);
//        } catch (DataAccessException e) {
//            System.err.println("Error checking exchange rate existence: " + e.getMessage());
//            return false;
//        }
//    }
//}

package bard.dao;

import bard.exception.ConflictException;
import bard.exception.DatabaseException;
import bard.exception.exchangeRate.ExchangeRateAlreadyExistsException;
import bard.exception.exchangeRate.ExchangeRateNotFoundException;
import bard.exception.exchangeRate.InvalidExchangeRateException;
import bard.model.Currency;
import bard.model.ExchangeRate;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Repository
public class ExchangeRateRepository {

    private final RowMapper<ExchangeRate> exchangeRateRowMapper = (rs, rowNum) -> {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setId(rs.getLong("er_id"));
        exchangeRate.setRate(rs.getBigDecimal("er_rate"));

        // Базовая валюта - используем точные названия из CREATE TABLE
        Currency baseCurrency = new Currency();
        baseCurrency.setId(rs.getLong("bc_id"));
        baseCurrency.setCode(rs.getString("bc_code"));
        baseCurrency.setFullName(rs.getString("bc_full_name"));
        baseCurrency.setSign(rs.getString("bc_sign"));
        exchangeRate.setBaseCurrency(baseCurrency);

        // Целевая валюта
        Currency targetCurrency = new Currency();
        targetCurrency.setId(rs.getLong("tc_id"));
        targetCurrency.setCode(rs.getString("tc_code"));
        targetCurrency.setFullName(rs.getString("tc_full_name"));
        targetCurrency.setSign(rs.getString("tc_sign"));
        exchangeRate.setTargetCurrency(targetCurrency);

        return exchangeRate;
    };
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ExchangeRate> getExchangeRates() {
        try {
            log.info("Начало выполнения getExchangeRates");

            String sql = """
                    SELECT 
                        er.id as er_id, 
                        er.rate as er_rate,
                        bc.id as bc_id,
                        bc.code as bc_code,
                        bc.full_name as bc_full_name,
                        bc.sign as bc_sign,
                        tc.id as tc_id,
                        tc.code as tc_code, 
                        tc.full_name as tc_full_name, 
                        tc.sign as tc_sign
                    FROM exchange_rates er
                    JOIN currencies bc ON er.base_currency_id = bc.id
                    JOIN currencies tc ON er.target_currency_id = tc.id
                    """;


            log.info("SQL: " + sql);

            List<ExchangeRate> result = jdbcTemplate.query(sql, exchangeRateRowMapper);
            log.info("Найдено записей: " + result.size());

            return result;
        } catch (DataAccessException e) {
            log.info("Ошибка в getExchangeRates: " + e.getMessage());
            throw new DatabaseException("Failed to fetch exchange rates: " + e.getMessage());
        } catch (Exception e) {
            log.info("Неожиданная ошибка в getExchangeRates: " + e.getMessage());
            throw new DatabaseException("Unexpected error: " + e.getMessage());
        }
    }

    public ExchangeRate getExchangeRateByCodes(String baseCode, String targetCode) {
        try {
            String sql = """
                    SELECT 
                        er.id as er_id, 
                        er.rate as er_rate,
                        bc.id as bc_id, 
                        bc.code as bc_code, 
                        bc.full_name as bc_full_name, 
                        bc.sign as bc_sign,
                        tc.id as tc_id,
                        tc.code as tc_code,
                        tc.full_name as tc_full_name, 
                        tc.sign as tc_sign
                    FROM exchange_rates er
                    JOIN currencies bc ON er.base_currency_id = bc.id
                    JOIN currencies tc ON er.target_currency_id = tc.id
                    WHERE bc.code = ? AND tc.code = ?
                    """;
            return jdbcTemplate.queryForObject(sql, exchangeRateRowMapper, baseCode, targetCode);
        } catch (EmptyResultDataAccessException e) {
            throw new bard.exception.exchangeRate.ExchangeRateNotFoundException(baseCode, targetCode);
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to fetch exchange rate: " + e.getMessage());
        }
    }

    public ExchangeRate postExchangeRate(ExchangeRate exchangeRate) {
        try {

            if (exchangeRate.getBaseCurrency().getCode().equals(exchangeRate.getTargetCurrency().getCode())) {
                throw new ConflictException(exchangeRate.getBaseCurrency().getCode());
            }

            if (exchangeRate.getRate().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new InvalidExchangeRateException(String.valueOf(exchangeRate.getRate()));
            }


            String checkSql = """
                    SELECT COUNT(*) FROM exchange_rates er
                    JOIN currencies bc ON er.base_currency_id = bc.id
                    JOIN currencies tc ON er.target_currency_id = tc.id
                    WHERE bc.code = ? AND tc.code = ?
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

            String sql = "INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setLong(1, exchangeRate.getBaseCurrency().getId());
                ps.setLong(2, exchangeRate.getTargetCurrency().getId());
                ps.setBigDecimal(3, exchangeRate.getRate());
                return ps;
            }, keyHolder);

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
                    UPDATE exchange_rates 
                    SET rate = ? 
                    WHERE base_currency_id = (SELECT id FROM currencies WHERE code = ?)
                    AND target_currency_id = (SELECT id FROM currencies WHERE code = ?)
                    """;

            int affected = jdbcTemplate.update(sql, newRate, baseCode, targetCode);

            if (affected == 0) {
                throw new ExchangeRateNotFoundException(baseCode, targetCode);
            }


            return getExchangeRateByCodes(baseCode, targetCode);

        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to update exchange rate: " + e.getMessage());
        }
    }

    public boolean isExchangeRateExists(String baseCurrencyCode, String targetCurrencyCode) {
        try {
            String checkSql = """
                    SELECT COUNT(*) FROM exchange_rates er
                    JOIN currencies bc ON er.base_currency_id = bc.id
                    JOIN currencies tc ON er.target_currency_id = tc.id
                    WHERE bc.code = ? AND tc.code = ?
                    """;
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class,
                    baseCurrencyCode,
                    targetCurrencyCode);

            return (count != null && count > 0);
        } catch (DataAccessException e) {
            System.err.println("Error checking exchange rate existence: " + e.getMessage());
            return false;
        }
    }
}
