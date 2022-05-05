package ru.iu3.backend.tools;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Класс - обработчик ошибок
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DataValidationException extends Exception {
    public DataValidationException(String message){
        super(message);
    }
}