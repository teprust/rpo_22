package ru.iu3.backend.auth;

import  org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;
import ru.iu3.backend.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/*
    @Component позволяет получить доступ к экземпляру класса через контекст приложения.
    Нам не нужно будет проделывать такое самостоятельно, но экземпляр класса AuthenticationProvider
    будет встроен в схему аутентификации spring boot security.
 */
@Component
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    UserRepository userRepository;

    // @Value является аннотацией Java, которая используется на уровне параметра поля или метода / конструктора и указывает значение по умолчанию
    @Value("${private.session-timeout}")
    private int sessionTimeout;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    /*
    Метод retrieveUser нужен для извлечения информации о пользователе, в нашем случае из таблицы users в
    базе данных. Обратите внимание на то, что в этом коде встречаются два различных типа с одним именем
    User. Один объявлен здесь:import org.springframework.security.core.userdetails.User; а другой у нас в
    модели User.
     */

    /*
    Итак, провайдер получает имя пользователя и токен, извлекает из базы данных пользователя с заданными
    логином и токеном, и возвращает информацию о пользователе в нужном формате. Если пользователь не
    найден потому что его нет в базе, или его токен отличается от заданного или отсутствует, метод генерирует
    исключение. Следствие этого – отказ в авторизации.
     */

    /*
    Нам нужен доступ к таблице users, который здесь уже есть. Здесь все просто, сравниваем время в поле
    activity с текущим и, если разница между ними больше минуты, закрываем сессию удаляя токен из базы. Если
    нет, то надо обновить в базе время последнего запроса пользователя.
     */

    @Override
    protected UserDetails retrieveUser(String userName, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
                Object token = usernamePasswordAuthenticationToken.getCredentials();
                Optional<ru.iu3.backend.models.User> uu = userRepository.findByToken(String.valueOf(token));
                if (!uu.isPresent())
                    throw new UsernameNotFoundException("user is not found");
                    ru.iu3.backend.models.User u = uu.get();
                    boolean timeout = true;
                    LocalDateTime dt = LocalDateTime.now();
                    if (u.activity != null) {
                        LocalDateTime nt = u.activity.plusMinutes(sessionTimeout);
                        if (dt.isBefore(nt))
                             timeout = false;
                    }
                    if (timeout) {
                        u.token = null;
                        userRepository.save(u);
                        throw new NonceExpiredException("session is expired");
                    }
                    else {
                        u.activity = dt;
                        userRepository.save(u);
                    }
                    UserDetails user= new User(u.login, u.password,
                true,
                true,
                true,
                true,
                AuthorityUtils.createAuthorityList("USER"));
                return user;
            }
}