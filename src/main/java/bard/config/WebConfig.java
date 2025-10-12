package bard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@ComponentScan("bard")
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:identifier.sqlite");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        initializeDatabase(jdbcTemplate);
        return jdbcTemplate;
    }

    private void initializeDatabase(JdbcTemplate jdbcTemplate) {
        try {
            System.out.println("🔄 Начинаем инициализацию базы данных...");

            String createCurrenciesTable = """
                    CREATE TABLE IF NOT EXISTS Currencies (
                        ID INTEGER PRIMARY KEY AUTOINCREMENT,
                        Code VARCHAR(3) NOT NULL UNIQUE,
                        FullName VARCHAR(100) NOT NULL,
                        Sign VARCHAR(5) NOT NULL
                    )
                    """;
//todo
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
            System.out.println("✅ Таблицы созданы/проверены");

            String checkDataSQL = "SELECT COUNT(*) FROM Currencies";
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
                System.out.println("✅ Валюты добавлены");

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
                System.out.println("✅ Курсы обмена добавлены");
            }

            System.out.println("🎯 База данных готова к работе");

        } catch (Exception e) {
            System.err.println("❌ Ошибка инициализации БД: " + e.getMessage());
            e.printStackTrace();
        }
    }
}