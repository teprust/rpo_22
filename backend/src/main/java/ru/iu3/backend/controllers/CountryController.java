package ru.iu3.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.models.Artist;
import ru.iu3.backend.models.Country;
import ru.iu3.backend.repositories.CountryRepository;

import java.util.*;

@RestController
@RequestMapping("/api/v1")

public class CountryController {
    //Ссылка на объект, содержащий базовые функции, описаннные в репозитории
    @Autowired
    CountryRepository countryRepository;

    /* Метод getAllCountries возвращает список стран,
   который будет автоматически преобразован в JSON.
   Заметим, что мы не делали сами реализацию метода
   findAll. Она уже есть в реализации интерфейса CountryRepository.*/

    @GetMapping("/countries")
    public List
    getAllCountries() {
        return countryRepository.findAll();
    }

    //Метод для добавления страны с проверкой на уникальность данных
    // ResponseEntity возвращает типы ошибок
    @PostMapping("/countries")
    public ResponseEntity<Object> createCountry(@RequestBody Country country)
            throws Exception {
        try {
            Country nc = countryRepository.save(country);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex) {
            String error;
            if (ex.getMessage().contains("countries.name_UNIQUE"))
                error = "countryalreadyexists";
            else
                error = "undefinederror";
            Map<String, String>
                    map = new HashMap<>();
            map.put("error", error);
            return ResponseEntity.ok(map);
            //return new ResponseEntity<Object> (map, HttpStatus.OK);
        }
    }
    //Список художников данной страны
    @GetMapping("/countries/{id}/artists")
    public ResponseEntity<List<Artist>> getCountryArtists(@PathVariable(value = "id") Long countryID){
            Optional<Country> cc = countryRepository.findById(countryID);
            if (cc.isPresent()) {
            return ResponseEntity.ok(cc.get().artists);
            }
            return ResponseEntity.ok(new ArrayList<Artist>());
    }

    //Проверка опечаток в названии страны
    /*Здесь обработка ошибки сводится к тому, что мы возвращаем код 404 на запрос в случае, если страна с
    указанным ключом отсутствует.*/

    @PutMapping("/countries/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable(value = "id") Long countryId,
            @RequestBody Country countryDetails) {
        Country country = null;
        Optional<Country>
                cc = countryRepository.findById(countryId);
        if (cc.isPresent()) {
            country = cc.get();
            country.name = countryDetails.name;
            countryRepository.save(country);
            return ResponseEntity.ok(country);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "country not found");
        }
    }
    /*Заметьте, что мы не передаем id в JSON редактируемой страны. Обработчик берет его не из объекта Country а
        из @PathVariable. Обратите внимание на разные способы передачи аргументов обработчикам запросов. Это
        может быть тело POST запроса (@RequestBody), элемент пути из URL (@PathVariable) или раздел аргументов
        в строке URL ( такого примера у нас пока не было).*/


    //Метод удаления записи из таблицы countries
    @DeleteMapping("/countries/{id}")
    public ResponseEntity<Object> deleteCountry(@PathVariable(value = "id") Long countryId){
    Optional<Country> country = countryRepository.findById(countryId);
    Map<String, Boolean> resp = new HashMap<>();
    if (country.isPresent()) {
        countryRepository.delete(country.get());
        resp.put("deleted", Boolean.TRUE);
    }
    else
        resp.put("deleted", Boolean.FALSE);
    return ResponseEntity.ok(resp);
    }
}

