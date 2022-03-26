package ru.iu3.backend.repositories;

/*реализует базовый набор функций, позволяющий извлекать,
модифицировать и удалять записи из таблицы Country*/

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iu3.backend.models.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}
