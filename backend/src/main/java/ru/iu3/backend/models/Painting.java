package ru.iu3.backend.models;
import javax.persistence.*;

/*Модель Painting - это класс, который повторяет структуру записи таблицы museum в базе данных. Для связи
        класса с таблицей, в базе используются, так называемые, аннотации - выражения начинающиеся с символа
        @, размещаемые перед объявлением класса Painting или методов этого класса и содержащие
        дополнительную информацию для компилятора, позволяющую отобразить экземпляр класса в запись
        таблицы и наоборот.*/
@Entity // указывается, что это таблица в базе
@Table(name = "paintings") // имя это таблицы
/* AccessType.FIELD указывает на то, что мы разрешаем доступ к полям класса вместо того, чтобы для
        каждого поля делать методы чтения и записи*/
@Access(AccessType.FIELD)

public class Painting{
    public Painting() { }
    public Painting (Long id) {
        this.id = id;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public Long id;

    @Column(name = "name", nullable = false)
    public String name;

    @ManyToOne
    @JoinColumn(name = "artistid")
    public Artist artistid;
    @ManyToOne

    @JoinColumn(name = "museumid")
    public Museum museumid;

    @Column(name = "year")
    public String year;
}