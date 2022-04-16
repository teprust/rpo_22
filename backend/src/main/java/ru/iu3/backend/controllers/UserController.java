package ru.iu3.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;

import ru.iu3.backend.models.Museum;
import ru.iu3.backend.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.backend.repositories.UserRepository;
import ru.iu3.backend.repositories.MuseumRepository;
import java.util.*;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")

public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MuseumRepository museumRepository;


    @GetMapping("/users")
    public List getAllUsers() {
        return userRepository.findAll();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId, @RequestBody User userDetails) {
        User user = null;
        Optional <User> uu = userRepository.findById(userId);
        if (uu.isPresent()) {
            user = uu.get();
            user.login = userDetails.login;
            user.email = userDetails.email;
            userRepository.save(user);
            return ResponseEntity.ok(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody User users) throws Exception {
        try {
            User nu = userRepository.save(users);
            return new ResponseEntity<Object>(nu, HttpStatus.OK);
        } catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("user.name_UNIQUE"))
                error = "useralreadyexists";
            else
                error = "undefinederror";
            Map<String, String> map = new HashMap<>();
            map.put("error", error);
            return ResponseEntity.ok(map);
        }
    }

    //Передача списка музеев
    @PostMapping("/users/{id}/addmuseums")
    public ResponseEntity<Object> addMuseums(@PathVariable(value = "id") Long userID, @Validated @RequestBody Set<Museum> museums) {
        // Извлекаем пользователя по конкретному ID
        Optional<User> uu = userRepository.findById(userID);
        int cnt = 0;

        if (uu.isPresent()) {
            User u = uu.get();
            // Если музеев несколько (добавляем их поочерёдно)
            for(Museum m: museums) {
                // Если есть музей, то мы его добавляем
                Optional<Museum> mm = museumRepository.findById(m.id);
                if (mm.isPresent()) {
                    u.addMuseum(mm.get());
                    ++cnt;
                }
            }
            // Сохраняем
            userRepository.save(u);
        }
        // Формируем
        Map<String, String> response = new HashMap<>();
        response.put("added", String.valueOf(cnt));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{id}/removemuseums")
    public ResponseEntity<Object> removeMuseums(@PathVariable(value = "id") Long userId, @Validated @RequestBody Set<Museum> museums) {
        Optional<User> uu = userRepository.findById(userId);
        int cnt = 0;
        if (uu.isPresent()) {
            User u = uu.get();
            for (Museum m : u.museums) {
                u.removeMuseum(m);
                cnt++;
            }
            userRepository.save(u);
        }
        Map<String, String> response = new HashMap<>();
        response.put("count", String.valueOf(cnt));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Map<String, Boolean> resp = new HashMap<>();
        if (user.isPresent()) {
            userRepository.delete(user.get());
            resp.put("deleted", Boolean.TRUE);
        } else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }
}
