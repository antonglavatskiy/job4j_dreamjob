package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.*;

import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oCandidateRepositoryTest.class
                .getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");

        DatasourceConfiguration configuration = new DatasourceConfiguration();
        DataSource datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    delete from users
                    """;
            connection.createQuery(sql).executeUpdate();
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        User user = sql2oUserRepository.save(new User(0, "user@user.ru",
                "user", "pass")).get();
        User savedUser = sql2oUserRepository.findByEmailAndPassword("user@user.ru", "pass").get();
        assertThat(user).usingRecursiveComparison().isEqualTo(savedUser);
    }

    @Test
    public void whenSaveThenFindUserByEmailAndPassword() {
        String email = "user@user.ru";
        String password = "pass";
        User user = sql2oUserRepository.save(new User(0, email, "user", password)).get();
        User savedUser = sql2oUserRepository.findByEmailAndPassword(email, password).get();
        assertThat(savedUser.getEmail()).isEqualTo(email);
        assertThat(savedUser.getPassword()).isEqualTo(password);
    }

    @Test
    public void whenSaveTwoUsersWithEqualsEmailThen() {
        String email = "user@user.ru";
        User user1 = new User(1, email, "user1", "pass1");
        User user2 = new User(2, email, "user2", "pass2");
        sql2oUserRepository.save(user1);
        assertThat(sql2oUserRepository.save(user2)).isEmpty();
        User savedUser = sql2oUserRepository.findByEmailAndPassword(email, "pass1").get();
        assertThat(user1).usingRecursiveComparison().isEqualTo(savedUser);
    }
}