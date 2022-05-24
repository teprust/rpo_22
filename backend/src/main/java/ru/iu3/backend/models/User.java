package ru.iu3.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Access(AccessType.FIELD)

public class User {

    public User() { }
    public User(Long id) {
        this.id = id;
    }

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    /*Обратите внимание на то, что поля password и salt отмечены аннотацией @JsonIgnore. Их конечно не надо
    светить в JSON, за пределами REST сервиса.*/

    // Поле логина
    @Column(name = "login", unique = true, nullable = false)
    public String login;

    @JsonIgnore
    @Column(name = "password")
    public String password;

    @Column(name = "email", unique = true, nullable = false)
    public String email;

    @JsonIgnore
    @Column(name = "salt")
    public String salt;

    //@JsonIgnore
    @Column(name = "token")
    public String token;

    // Здесь будет храниться время, когда мы последний раз видели
    // пользователя
    @Column(name = "activity")
    public LocalDateTime activity;

    @Transient
    public String np;

//    Для типа List, именно в этом случае, JPA генерирует очень неэффективный код
//    запросов к базе данных. Используете всегда Set для отношений многие-ко-многим.
    @ManyToMany(mappedBy = "users")
    public Set<Museum> museums = new HashSet<>();

    /**
     * Метод добавляет музей. По сути, он просто сокращает нам работу
     * @param m - структура музея
     */
    public void addMuseum(Museum m) {
        this.museums.add(m);
        m.users.add(this);
    }

    /**
     * Метод, который осуществляет удаления музея
     * @param m - структура данных музея
     */
    public void removeMuseum(Museum m) {
        this.museums.remove(m);
        m.users.remove(this);
    }
}
