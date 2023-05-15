package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private final AtomicInteger nextId = new AtomicInteger();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    public MemoryCandidateRepository() {
        save(new Candidate(0, "Ivanov", "Junior java developer", LocalDateTime.now(), 1));
        save(new Candidate(0, "Petrov", "Middle java developer", LocalDateTime.now(), 2));
        save(new Candidate(0, "Popov", "Senior java developer", LocalDateTime.now(), 3));
        save(new Candidate(0, "Smirnov", "Intern java developer", LocalDateTime.now(), 1));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
        candidates.putIfAbsent(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(),
                (id, oldCandidate) ->
                        new Candidate(oldCandidate.getId(), candidate.getName(),
                                candidate.getDescription(), LocalDateTime.now(),
                                candidate.getCityId())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
