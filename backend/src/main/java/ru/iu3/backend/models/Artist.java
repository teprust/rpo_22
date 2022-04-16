package ru.iu3.backend.models;
import javax.persistence.*;
@Entity
@Table(name = "artists")
@Access(AccessType.FIELD)
public class Artist {

   /* В модели Artist есть аннотация @ManyToOne - многие к одному, которая задает зависимость между
    таблицами artists и countries. Договоримся, что будем указывать страну в которой художник родился или
    работал. Только одну для каждого. Тогда отношение многие к одному нам подходит. Отношение в модели
    Artist необходимо для того чтобы можно было задать, изменить или получить страну художника.*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public Long id;
    @Column(name = "name", nullable = false, unique = true)
    public String name;
    @Column(name = "age", nullable = false)
    public String age;
    @ManyToOne()
    @JoinColumn(name = "countryid")
    public Country countryid;

    public Artist() { }
    public Artist(Long id) {
        this.id = id;
    }

}