package ru.yandex.practicum.filmorate.exceptions;

/**
 * Исключения для проверки валидации.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
