package ru.iu3.backend.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.iu3.backend.models.Artist;
import ru.iu3.backend.models.Country;
import ru.iu3.backend.repositories.CountryRepository;
import ru.iu3.backend.tools.DataValidationException;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")

public class CountryController {
    //Ссылка на объект, содержащий базовые функции, описанные в репозитории
    @Autowired
    CountryRepository countryRepository;

    /* Метод getAllCountries возвращает список стран,
   который будет автоматически преобразован в JSON.
   Заметим, что мы не делали сами реализацию метода
   findAll. Она уже есть в реализации интерфейса CountryRepository.*/


    @GetMapping("/countries")
    public List getAllCountries() {
        return countryRepository.findAll();
    }

    //Метод для добавления страны с проверкой на уникальность данных
    // ResponseEntity возвращает типы ошибок
    @PostMapping("/countries")
    public ResponseEntity<Object> createCountry(@Valid @RequestBody Country country)
            throws DataValidationException {
        try {
            Country nc = countryRepository.save(country);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex) {
            String error;
            if (ex.getMessage().contains("countries.name_UNIQUE"))
                throw new DataValidationException("Эта страна уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
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

    @GetMapping("/countries/{id}")
    public ResponseEntity<Country> ResponseEntityGetCountry(@PathVariable(value = "id") Long countryId)
            throws DataValidationException {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(()->new DataValidationException("Страна с таким индексом не найдена"));
        return ResponseEntity.ok(country);
    }

    @PutMapping("/countries/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable(value = "id") Long countryId, @Valid @RequestBody Country countryDetails)
            throws DataValidationException {
        try {
            Country country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new DataValidationException("Страна с таким индексом не найдена"));
            country.name = countryDetails.name;
            countryRepository.save(country);
            return ResponseEntity.ok(country);
        }
        catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("countries.name_UNIQUE"))
                throw new DataValidationException("Эта страна уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }
    /*Заметьте, что мы не передаем id в JSON редактируемой страны. Обработчик берет его не из объекта Country а
        из @PathVariable. Обратите внимание на разные способы передачи аргументов обработчикам запросов. Это
        может быть тело POST запроса (@RequestBody), элемент пути из URL (@PathVariable) или раздел аргументов
        в строке URL ( такого примера у нас пока не было).*/


    //Метод удаления записи из таблицы countries
    @PostMapping("/deletecountries")
    public ResponseEntity deleteCountries(@Valid @RequestBody List<Country> countries) {
        countryRepository.deleteAll(countries);
        return new ResponseEntity(HttpStatus.OK);
    }
}

