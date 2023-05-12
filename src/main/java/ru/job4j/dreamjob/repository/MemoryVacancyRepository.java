package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryVacancyRepository implements VacancyRepository {

    private final AtomicInteger nextId = new AtomicInteger();

    private final Map<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

    public MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "Vacancy for intern", LocalDateTime.now()));
        save(new Vacancy(0, "Junior Java Developer", "Vacancy for junior", LocalDateTime.now()));
        save(new Vacancy(0, "Junior+ Java Developer", "Vacancy for junior+", LocalDateTime.now()));
        save(new Vacancy(0, "Middle Java Developer", "Vacancy for middle", LocalDateTime.now()));
        save(new Vacancy(0, "Middle+ Java Developer", "Vacancy for middle+", LocalDateTime.now()));
        save(new Vacancy(0, "Senior Java Developer", "Vacancy for senior", LocalDateTime.now()));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.incrementAndGet());
        vacancies.putIfAbsent(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(),
                (id, oldVacancy) ->
                        new Vacancy(oldVacancy.getId(), vacancy.getTitle(),
                                vacancy.getDescription(), LocalDateTime.now())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}
