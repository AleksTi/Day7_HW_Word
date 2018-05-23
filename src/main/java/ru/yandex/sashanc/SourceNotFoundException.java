package ru.yandex.sashanc;

/**
 * Исключение выбрасываемое в случае отсутствия источников в массиве
 */
public class SourceNotFoundException extends Exception {
    public SourceNotFoundException(String message) {
        super(message);
    }
}
