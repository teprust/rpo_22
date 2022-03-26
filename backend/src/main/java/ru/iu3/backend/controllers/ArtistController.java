package ru.iu3.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.models.Artist;
import ru.iu3.backend.repositories.ArtistRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")

public class ArtistController {
    @Autowired
    ArtistRepository artistRepository;

    /* Метод getAllCountries возвращает список художников,
   который будет автоматически преобразован в JSON.
   Заметим, что мы не делали сами реализацию метода
   findAll. Она уже есть в реализации интерфейса ArtistRepository.*/

    @GetMapping("/artists")
    public List
    getAllArtists() {  return artistRepository.findAll(); }

    //Метод для добавления страны с проверкой на уникальность данных

    @PostMapping("/artists")
    public ResponseEntity<Object> createArtist(@RequestBody Artist artist)
            throws Exception {
        try {
            Artist na = artistRepository.save(artist);
            return new ResponseEntity<Object>(na, HttpStatus.OK);
        }
        catch(Exception ex) {
            String error;
            if (ex.getMessage().contains("artists.name_UNIQUE"))
                error = "countyalreadyexists";
            else
                error = "undefinederror";
            Map<String, String>
                    map = new HashMap<>();
            map.put("error", error);
            return ResponseEntity.ok(map);
            //return new ResponseEntity<Object> (map, HttpStatus.OK);
        }
    }

    //Проверка опечаток в названии страны
    /*Здесь обработка ошибки сводится к тому, что мы возвращаем код 404 на запрос в случае, если страна с
    указанным ключом отсутствует.*/

    @PutMapping("/artists/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable(value = "id") Long artistId,
                                                 @RequestBody Artist artistDetails) {
        Artist artist = null;
        Optional<Artist>
                cc = artistRepository.findById(artistId);
        if (cc.isPresent()) {
            artist = cc.get();
            artist.name = artistDetails.name;
            artistRepository.save(artist);
            return ResponseEntity.ok(artist);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "artist not found");
        }
    }
    /*Заметьте, что мы не передаем id в JSON редактируемой страны. Обработчик берет его не из объекта Artist а
        из @PathVariable. Обратите внимание на разные способы передачи аргументов обработчикам запросов. Это
        может быть тело POST запроса (@RequestBody), элемент пути из URL (@PathVariable) или раздел аргументов
        в строке URL ( такого примера у нас пока не было).*/


    //Метод удаления записи из таблицы artists
    @DeleteMapping("/artists/{id}")
    public ResponseEntity<Object> deleteArtist(@PathVariable(value = "id") Long artistId){
        Optional<Artist> artist = artistRepository.findById(artistId);
        Map<String, Boolean> resp = new HashMap<>();
        if (artist.isPresent()) {
            artistRepository.delete(artist.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }
}
