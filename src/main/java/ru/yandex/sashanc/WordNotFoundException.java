package ru.yandex.sashanc;

/**
 * Выбрасываемое исключение в случае отсутствия слов для поиска в массиве
 */
public class WordNotFoundException extends Exception {
    public WordNotFoundException(String message) {
        super(message);
    }
}
