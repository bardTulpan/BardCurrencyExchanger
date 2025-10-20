package bard.service;

import bard.dao.ExchangeRateRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Slf4j
@Service
public class DatabaseInitializer {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @PostConstruct
    public void initializeDatabase() {
        try {
            log.info("Начинаем инициализацию базы данных...");

            String createCurrenciesTable = """
                    CREATE TABLE IF NOT EXISTS Currencies (
                        ID INTEGER PRIMARY KEY AUTOINCREMENT,
                        Code VARCHAR(3) NOT NULL UNIQUE,
                        FullName VARCHAR(100) NOT NULL,
                        Sign VARCHAR(5) NOT NULL
                    )
                    """;
            String createExchangeRatesTable = """
                    CREATE TABLE IF NOT EXISTS ExchangeRates (
                        ID INTEGER PRIMARY KEY AUTOINCREMENT,
                        BaseCurrencyId INTEGER NOT NULL,
                        TargetCurrencyId INTEGER NOT NULL,
                        Rate DECIMAL(12,6) NOT NULL,
                        FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies(ID),
                        FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies(ID),
                        UNIQUE (BaseCurrencyId, TargetCurrencyId)
                    )
                    """;

            jdbcTemplate.execute(createCurrenciesTable);
            jdbcTemplate.execute(createExchangeRatesTable);
            log.info("Таблицы созданы/проверены");

            String checkDataSQL = "SELECT COUNT(ID) FROM Currencies";
            Integer count = jdbcTemplate.queryForObject(checkDataSQL, Integer.class);

            if (count == 0) {
                String insertCurrenciesSQL = """
                        INSERT INTO Currencies (Code, FullName, Sign) VALUES
                        ('USD', 'US Dollar', '$'),
                        ('EUR', 'Euro', '€'),
                        ('GBP', 'British Pound', '£'),
                        ('RUB', 'Russian Ruble', '₽')
                        """;
                jdbcTemplate.execute(insertCurrenciesSQL);
                log.info("Валюты добавлены");

                String insertExchangeRatesSQL = """
                        INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES
                        (1, 2, 0.85),   -- USD to EUR
                        (1, 3, 0.73),   -- USD to GBP
                        (1, 4, 75.50),  -- USD to RUB
                        (2, 1, 1.18),   -- EUR to USD
                        (2, 3, 0.86),   -- EUR to GBP
                        (2, 4, 88.75),  -- EUR to RUB
                        (3, 1, 1.37),   -- GBP to USD
                        (3, 2, 1.16),   -- GBP to EUR
                        (3, 4, 103.40), -- GBP to RUB
                        (4, 1, 0.0132), -- RUB to USD
                        (4, 2, 0.0113), -- RUB to EUR
                        (4, 3, 0.0097)  -- RUB to GBP
                        """;
                jdbcTemplate.execute(insertExchangeRatesSQL);
                log.info("Курсы обмена добавлены");
            }
            log.info("База данных готова к работе");
        } catch (Exception e) {
            log.error("Ошибка инициализации БД: {}", e.getMessage(), e);
        }
    }

}
