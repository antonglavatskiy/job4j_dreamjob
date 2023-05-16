package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.Candidate;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oCandidateRepository implements CandidateRepository {

    private final Sql2o sql2o;

    public Sql2oCandidateRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Candidate save(Candidate candidate) {
        try (Connection collection = sql2o.open()) {
            String sql = """
                      insert into candidates (name, description, creation_date, city_id, file_id)
                      values (:name, :description, :creationDate, :cityId, :fileId)
                      """;
            Query query = collection.createQuery(sql)
                    .addParameter("name", candidate.getName())
                    .addParameter("description", candidate.getDescription())
                    .addParameter("creationDate", candidate.getCreationDate())
                    .addParameter("cityId", candidate.getCityId())
                    .addParameter("fileId", candidate.getFileId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            candidate.setId(generatedId);
            return candidate;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    delete from candidates
                    where id = :id
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("id", id);
            int affectedRows = query.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }

    @Override
    public boolean update(Candidate candidate) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    update candidates
                    set name = :name, description = :description,
                        creation_date = :creationDate,
                        city_id = :cityId, file_id = :fileId
                    where id = :id
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("name", candidate.getName())
                    .addParameter("description", candidate.getDescription())
                    .addParameter("creationDate", candidate.getCreationDate())
                    .addParameter("cityId", candidate.getCityId())
                    .addParameter("fileId", candidate.getFileId())
                    .addParameter("id", candidate.getId());
            int affectedRows = query.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }

    @Override
    public Optional<Candidate> findById(int id) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    select * from candidates
                    where id = :id
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("id", id);
            Candidate candidate = query.setColumnMappings(Candidate.COLUMN_MAPPING)
                    .executeAndFetchFirst(Candidate.class);
            return Optional.ofNullable(candidate);
        }
    }

    @Override
    public Collection<Candidate> findAll() {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    select * from candidates
                    """;
            Query query = connection.createQuery(sql);
            return query.setColumnMappings(Candidate.COLUMN_MAPPING)
                    .executeAndFetch(Candidate.class);
        }
    }
}
