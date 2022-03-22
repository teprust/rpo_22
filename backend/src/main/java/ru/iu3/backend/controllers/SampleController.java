package ru.iu3.backend.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/v1")
public class SampleController {
    @GetMapping("/title") // методу getTitle будт передаваться клиентские GET запросы с локальной часть URL сложенной из /api/v1 и /title .
    public String getTitle() {
        return "<title>Hello from Back-end</title>";
    }
}