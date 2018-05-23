package ru.yandex.sashanc;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * @author Aleksandr Tikhonov
 * date of modidication 28.04.2018
 */
public class Main {
    final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws WordNotFoundException, IOException, SourceNotFoundException {

        //Аргументы метода, должны быть получены извне. Пока создаются в самом методе.
        String[] sourceList = new String[2000];
        String[] wordList = new String[200];
        String fileRes = "E://work/res.txt";

        for (int i = 0; i < 10; i++) {
            sourceList[i] = "//E://work/source" + i + ".txt";
        }
        sourceList[10] = "https://habrahabr.main.java.sashanc.ru/post/260773/";
        sourceList[11] = "http://lib.main.java.sashanc.ru/TALES/BIANKI/anutka.txt";
        //Ресурс 12 добавлен чтобы вызвать ошибку
        sourceList[12] = "//E://work/filenotexist";

        wordList[0] = "животные";
        wordList[1] = "их";
        wordList[2] = "право";
        wordList[3] = "лиса";
        wordList[4] = "мех";
        wordList[5] = "Сегодня";
        wordList[6] = "утка";

        Occurrence oc = new Occurrence();
        oc.getOccurences(sourceList, wordList, fileRes);

        ApplicationContext context = new AnnotationConfigApplicationContext();
    }
}
