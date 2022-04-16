package ru.iu3.backend.models;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*Модель Museum - это класс, который повторяет структуру записи таблицы museum в базе данных. Для связи
        класса с таблицей, в базе используются, так называемые, аннотации - выражения начинающиеся с символа
        @, размещаемые перед объявлением класса Museum или методов этого класса и содержащие
        дополнительную информацию для компилятора, позволяющую отобразить экземпляр класса в запись
        таблицы и наоборот.*/
@Entity // указывается, что это таблица в базе
@Table(name = "museums") // имя это таблицы
/* AccessType.FIELD указывает на то, что мы разрешаем доступ к полям класса вместо того, чтобы для
        каждого поля делать методы чтения и записи*/
@Access(AccessType.FIELD)

public class Museum {
    public Museum() { }
    public Museum(Long id) {
        this.id = id;
    }
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    @Column(name = "name", nullable = false, unique = true)
    public String name;
    @Column(name = "location", nullable = false)
    public String location;
    @JsonIgnore
    @OneToMany
    public List<Painting>
            paintings = new ArrayList<>();
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "usersmuseums", joinColumns = @JoinColumn(name = "museumid"), inverseJoinColumns = @JoinColumn(name = "userid"))
            public Set<User>
            users = new HashSet<>();
}