package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.File;

import java.util.Optional;

@Repository
public class Sql2oFileRepository implements FileRepository {

    private final Sql2o sql2o;

    public Sql2oFileRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public File save(File file) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    insert into files (name, path)
                    values (:name, :path)
                    """;
            Query query = connection.createQuery(sql, true)
                    .addParameter("name", file.getName())
                    .addParameter("path", file.getPath());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            file.setId(generatedId);
            return file;
        }
    }

    @Override
    public Optional<File> findById(int id) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    select * from files
                    where id = :id
                    """;
            Query query = connection.createQuery(sql);
            File file = query.addParameter("id", id)
                             .executeAndFetchFirst(File.class);
            return Optional.ofNullable(file);
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    delete from files
                    where id = :id
                    """;
            Query query = connection.createQuery(sql);
            int affectedRows = query.addParameter("id", id)
                                    .executeUpdate()
                                    .getResult();
            return affectedRows > 0;
        }
    }
}
