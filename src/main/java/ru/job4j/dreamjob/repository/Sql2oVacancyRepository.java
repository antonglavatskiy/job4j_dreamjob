package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.Vacancy;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oVacancyRepository implements VacancyRepository {

    private final Sql2o sql2o;

    public Sql2oVacancyRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                      insert into vacancies (title, description, creation_date, visible, city_id, file_id)
                      values (:title, :description, :creationDate, :visible, :cityId, :fileId)
                      """;
            Query query = connection.createQuery(sql)
                    .addParameter("title", vacancy.getTitle())
                    .addParameter("description", vacancy.getDescription())
                    .addParameter("creationDate", vacancy.getCreationDate())
                    .addParameter("visible", vacancy.getVisible())
                    .addParameter("cityId", vacancy.getCityId())
                    .addParameter("fileId", vacancy.getFileId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            vacancy.setId(generatedId);
            return vacancy;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    delete from vacancies
                    where id = :id
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("id", id);
            int affectedRows = query.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }

    @Override
    public boolean update(Vacancy vacancy) {
        try (Connection connection = sql2o.open()) {
            var sql = """
                    update vacancies
                    set title = :title, description = :description,
                        creation_date = :creationDate, visible = :visible,
                        city_id = :cityId, file_id = :fileId
                    where id = :id
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("title", vacancy.getTitle())
                    .addParameter("description", vacancy.getDescription())
                    .addParameter("creationDate", vacancy.getCreationDate())
                    .addParameter("visible", vacancy.getVisible())
                    .addParameter("cityId", vacancy.getCityId())
                    .addParameter("fileId", vacancy.getFileId())
                    .addParameter("id", vacancy.getId());
            int affectedRows = query.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    select * from vacancies
                    where id = :id
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("id", id);
            Vacancy vacancy = query.setColumnMappings(Vacancy.COLUMN_MAPPING)
                    .executeAndFetchFirst(Vacancy.class);
            return Optional.ofNullable(vacancy);
        }
    }

    @Override
    public Collection<Vacancy> findAll() {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    select * from vacancies
                    """;
            Query query = connection.createQuery(sql);
            return query.setColumnMappings(Vacancy.COLUMN_MAPPING)
                    .executeAndFetch(Vacancy.class);
        }
    }
}
