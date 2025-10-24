package bard.service;

import lombok.extern.slf4j.Slf4j;
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
                        CREATE TABLE IF NOT EXISTS currencies (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            code VARCHAR(3) NOT NULL UNIQUE,
                            full_name VARCHAR(100) NOT NULL,
                            sign VARCHAR(5) NOT NULL
                        )
                    """;

            String createExchangeRatesTable = """
                        CREATE TABLE IF NOT EXISTS exchange_rates (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            base_currency_id INTEGER NOT NULL,
                            target_currency_id INTEGER NOT NULL,
                            rate DECIMAL(12,6) NOT NULL,
                            FOREIGN KEY (base_currency_id) REFERENCES currencies(id),
                            FOREIGN KEY (target_currency_id) REFERENCES currencies(id),
                            UNIQUE (base_currency_id, target_currency_id)
                        )
                    """;

            jdbcTemplate.execute(createCurrenciesTable);
            jdbcTemplate.execute(createExchangeRatesTable);
            log.info("Таблицы созданы/проверены");

            String checkDataSQL = "SELECT COUNT(id) FROM currencies";
            Integer count = jdbcTemplate.queryForObject(checkDataSQL, Integer.class);

            if (count == 0) {
                String insertCurrenciesSQL = """
                        INSERT INTO currencies (code, full_name, sign) VALUES
                        ('USD', 'US Dollar', '$'),
                        ('EUR', 'Euro', '€'),
                        ('GBP', 'British Pound', '£'),
                        ('RUB', 'Russian Ruble', '₽')
                        """;
                jdbcTemplate.execute(insertCurrenciesSQL);
                log.info("Валюты добавлены");

                String insertExchangeRatesSQL = """
                        INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES
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
