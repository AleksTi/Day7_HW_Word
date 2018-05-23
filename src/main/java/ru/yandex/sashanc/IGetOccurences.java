package ru.yandex.sashanc;

import java.io.IOException;

public interface IGetOccurences {
    void getOccurences(String[] sources, String[] words, String res) throws IOException, SourceNotFoundException, WordNotFoundException;
}
