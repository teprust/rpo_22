package ru.iu3.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.models.Museum;
import ru.iu3.backend.repositories.MuseumRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")

public class MuseumController {
    @Autowired
    MuseumRepository museumRepository;

    /* Метод getAllCountries возвращает список художников,
   который будет автоматически преобразован в JSON.
   Заметим, что мы не делали сами реализацию метода
   findAll. Она уже есть в реализации интерфейса museumRepository.*/

    @GetMapping("/museums")
    public List
    getAllMuseums() {
        return museumRepository.findAll();
    }

    //Метод для добавления страны с проверкой на уникальность данных

    @PostMapping("/museums")
    public ResponseEntity<Object> createMuseum(@RequestBody Museum museum)
            throws Exception {
        try {
            Museum nm = museumRepository.save(museum);
            return new ResponseEntity<Object>(nm, HttpStatus.OK);
        }
        catch(Exception ex) {
            String error;
            if (ex.getMessage().contains("museums.name_UNIQUE"))
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

    @PutMapping("/museums/{id}")
    public ResponseEntity<Museum> updateMuseum(@PathVariable(value = "id") Long museumId,
                                               @RequestBody Museum museumDetails) {
        Museum museum = null;
        Optional<Museum>
                cc = museumRepository.findById(museumId);
        if (cc.isPresent()) {
            museum = cc.get();
            museum.name = museumDetails.name;
            museumRepository.save(museum);
            return ResponseEntity.ok(museum);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "museum not found");
        }
    }
    /*Заметьте, что мы не передаем id в JSON редактируемой страны. Обработчик берет его не из объекта museum а
        из @PathVariable. Обратите внимание на разные способы передачи аргументов обработчикам запросов. Это
        может быть тело POST запроса (@RequestBody), элемент пути из URL (@PathVariable) или раздел аргументов
        в строке URL ( такого примера у нас пока не было).*/


    //Метод удаления записи из таблицы museums
    @DeleteMapping("/museums/{id}")
    public ResponseEntity<Object> deleteMuseum(@PathVariable(value = "id") Long museumId){
        Optional<Museum> museum = museumRepository.findById(museumId);
        Map<String, Boolean> resp = new HashMap<>();
        if (museum.isPresent()) {
            museumRepository.delete(museum.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }
}