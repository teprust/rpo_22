package ru.iu3.backend.repositories;

/*реализует базовый набор функций, позволяющий извлекать,
модифицировать и удалять записи из таблицы Painting*/

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iu3.backend.models.Painting;

@Repository
public interface PaintingRepository extends JpaRepository<Painting, Long> {
}
