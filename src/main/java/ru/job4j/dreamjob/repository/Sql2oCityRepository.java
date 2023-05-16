package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.City;

import java.util.Collection;

@Repository
public class Sql2oCityRepository implements CityRepository {

    private final Sql2o sql2o;

    public Sql2oCityRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<City> findAll() {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    select * from cities
                    """;
            Query query = connection.createQuery(sql);
            return query.executeAndFetch(City.class);
        }
    }
}
