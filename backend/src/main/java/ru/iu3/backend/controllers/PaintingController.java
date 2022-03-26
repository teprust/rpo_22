package ru.iu3.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.models.Painting;
import ru.iu3.backend.repositories.PaintingRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PaintingController {
    @Autowired
    PaintingRepository paintingRepository;

    /* Метод getAllPaintings возвращает список картин,
   который будет автоматически преобразован в JSON.
   Заметим, что мы не делали сами реализацию метода
   findAll. Она уже есть в реализации интерфейса PaintingRepository.*/

    @GetMapping("/paintings")
    public List
    getAllPaintings() {
        return paintingRepository.findAll();
    }

    //Метод для добавления страны с проверкой на уникальность данных

    @PostMapping("/paintings")
    public ResponseEntity<Object> createPainting(@RequestBody Painting painting)
            throws Exception {
        try {
            Painting nm = paintingRepository.save(painting);
            return new ResponseEntity<Object>(nm, HttpStatus.OK);
        }
        catch(Exception ex) {
            String error;
            if (ex.getMessage().contains("paintings.name_UNIQUE"))
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

    //Проверка опечаток в названии картины
    /*Здесь обработка ошибки сводится к тому, что мы возвращаем код 404 на запрос в случае, если страна с
    указанным ключом отсутствует.*/

    @PutMapping("/paintings/{id}")
    public ResponseEntity<Painting> updatePainting(@PathVariable(value = "id") Long paintingId,
                                               @RequestBody Painting paintingDetails) {
        Painting painting = null;
        Optional<Painting>
                cc = paintingRepository.findById(paintingId);
        if (cc.isPresent()) {
            painting = cc.get();
            painting.name = paintingDetails.name;
            paintingRepository.save(painting);
            return ResponseEntity.ok(painting);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "painting not found");
        }
    }
    /*Заметьте, что мы не передаем id в JSON редактируемой страны. Обработчик берет его не из объекта painting а
        из @PathVariable. Обратите внимание на разные способы передачи аргументов обработчикам запросов. Это
        может быть тело POST запроса (@RequestBody), элемент пути из URL (@PathVariable) или раздел аргументов
        в строке URL ( такого примера у нас пока не было).*/


    //Метод удаления записи из таблицы museums
    @DeleteMapping("/paintings/{id}")
    public ResponseEntity<Object> deletemuseum(@PathVariable(value = "id") Long paintingId){
        Optional<Painting> painting = paintingRepository.findById(paintingId);
        Map<String, Boolean> resp = new HashMap<>();
        if (painting.isPresent()) {
            paintingRepository.delete(painting.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }
}