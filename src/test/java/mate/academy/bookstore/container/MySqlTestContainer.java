package mate.academy.bookstore.container;

import org.testcontainers.containers.MySQLContainer;

public class MySqlTestContainer extends MySQLContainer<MySqlTestContainer> {
    private static final String DB_IMAGE = "mysql:8";

    private static MySqlTestContainer mysqlContainer;

    private MySqlTestContainer() {
        super(DB_IMAGE);
    }

    public static synchronized MySqlTestContainer getInstance() {
        if (mysqlContainer == null) {
            mysqlContainer = new MySqlTestContainer();
        }
        return mysqlContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", mysqlContainer.getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", mysqlContainer.getUsername());
        System.setProperty("TEST_DB_PASSWORD", mysqlContainer.getPassword());
    }

    @Override
    public void stop() {
    }
}
