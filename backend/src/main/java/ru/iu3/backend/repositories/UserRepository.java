package ru.iu3.backend.repositories;

/*реализует базовый набор функций, позволяющий извлекать,
модифицировать и удалять записи из таблицы User*/

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iu3.backend.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByToken(String valueOf);

    Optional<User> findByLogin(String login);
}

