package bard.dao;

import bard.exception.AlreadyExistsException;
import bard.exception.DatabaseException;
import bard.exception.NotFoundException;
import bard.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CurrencyRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Currency> currencyRowMapper = (rs, rowNum) -> {
        Currency currency = new Currency(
                rs.getString("code"),
                rs.getString("full_name"),
                rs.getString("sign")
        );
        currency.setId(rs.getLong("id"));
        return currency;
    };

    @Autowired
    public CurrencyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Currency> getCurrencies() {
        try {
            String sql = "SELECT id, code, full_name, sign from currencies";
            return jdbcTemplate.query(sql, currencyRowMapper);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to fetch currencies: " + e.getMessage());
        }
    }

    public Currency getCurrencyByCode(String code) {
        try {
            String sql = "SELECT id, code, full_name, sign FROM currencies WHERE code = ?";
            return jdbcTemplate.queryForObject(sql, currencyRowMapper, code);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(code);
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to fetch currency: " + e.getMessage());
        }
    }

    public Currency getCurrencyById(long id) {
        try {
            String sql = "SELECT id, code, full_name, sign FROM currencies WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, currencyRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(Long.toString(id)); //мб это плохая идея
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to fetch currency: " + e.getMessage());
        }
    }

    public void postCurrency(Currency currency) {
        try {
            String checkSql = "SELECT EXISTS(SELECT 1 FROM currencies WHERE code = ?)";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, currency.getCode());

            if (count != null && count > 0) {
                throw new AlreadyExistsException(currency.getCode());
            }

            String insertSql = "INSERT INTO currencies (code, full_mame, sign) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, currency.getCode(), currency.getFullName(), currency.getSign());

        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to insert currency: " + e.getMessage());
        }
    }
}
