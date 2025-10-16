package bard.dao;

import bard.exception.DatabaseException;
import bard.exception.ExchangeAlreadyExistsException;
import bard.exception.ExchangeNotFoundException;
import bard.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CurrencyRepository implements CurrencyImpl {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Currency> currencyRowMapper = (rs, rowNum) -> {
        Currency currency = new Currency(
                rs.getString("Code"),
                rs.getString("FullName"),
                rs.getString("Sign")
        );
        currency.setId(rs.getLong("ID"));
        return currency;
    };

    @Autowired
    public CurrencyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        System.out.print("gooida");

    }

    @Override
    public List<Currency> getCurrencies() {
        try {
            String sql = "SELECT * from Currencies";
            return jdbcTemplate.query(sql, currencyRowMapper);
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to fetch currencies: " + e.getMessage());
        }
    }

    @Override
    public Currency getCurrencyByCode(String code) {
        try {
            String sql = "SELECT * FROM Currencies WHERE Code = ?";
            return jdbcTemplate.queryForObject(sql, currencyRowMapper, code);
        } catch (EmptyResultDataAccessException e) {
            throw new ExchangeNotFoundException(code);
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to fetch currency: " + e.getMessage());
        }
    }

    @Override
    public Currency getCurrencyById(long id) {
        try {
            String sql = "SELECT * FROM Crrencies WHERE ID = ?";
            return jdbcTemplate.queryForObject(sql, currencyRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ExchangeNotFoundException(Long.toString(id)); //мб это плохая идея
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to fetch currency: " + e.getMessage());
        }
    }

    @Override
    public void postCurrency(Currency currency) {
        try {
            String checkSql = "SELECT COUNT(*) FROM Currencies WHERE Code = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, currency.getCode());

            if (count != null && count > 0) {
                throw new ExchangeAlreadyExistsException(currency.getCode());
            }

            String insertSql = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, currency.getCode(), currency.getFullName(), currency.getSign());

        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to insert currency: " + e.getMessage());
        }
    }
}
