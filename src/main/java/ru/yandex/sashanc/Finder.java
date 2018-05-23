package ru.yandex.sashanc;

import org.apache.log4j.Logger;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

/**
 * Класс Finder - объект класса получает на вход ресурс (файл, http, ...) и массив слов,
 * которые он ищет в ресурсе
 */
public class Finder implements Callable<List<String>> {
    final static Logger logger = Logger.getLogger(Finder.class);
    private String source;
    private String[] words;

    /**
     * Конструктор класса - получает на вход ресурс (файл, http, ...) и массив слов,
     * которые он ищет в ресурсе
     * @param source
     * @param words
     */
    public Finder(String source, String[] words) {
        this.source = source;
        this.words = words;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public List<String> call() throws IOException {
        List<String> resultList = new ArrayList<>();

        logger.info("Start parsing " + source);
        try (Scanner sc = new Scanner(new InputStreamReader(sourceParser(source)))) {

            String word;
            StringBuilder strBuilder = new StringBuilder();
            String[] sentences;
            while (sc.hasNext()) {
                strBuilder.append(sc.nextLine());
            }
            sentences = strBuilder.toString().split("(?<=\\.\\s)");
            for (String line : sentences) {
                for (int i = 0; i < words.length; i++) {
                    word = words[i];
                    if (word != null && line.matches(".*\\b" + word + "\\b.*")) {
                        resultList.add(line);
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * Метод проверяет наличие подстроки "http" или "www" в имени ресурса и, в случае наличия,
     * ресурс считается http интернет-ресурсом и открывается соответствующий поток InputStream
     * @param source - Имя ресурса
     * @return поток экземпляр InputStream
     * @throws IOException
     */
    public InputStream sourceParser(String source) throws IOException {
        InputStream is = null;
        if (source.matches(".*http.*") || source.contains("www")) {
            try {
                is = new URL(source).openConnection().getInputStream();
            } catch (MalformedURLException e) {
                //e.printStackTrace();
                logger.error("MalformedURLException is occured in " + source);
                throw new MalformedURLException();
            } catch (IOException e) {
                //e.printStackTrace();
                logger.error("IOException is occured in " + source);
                throw new IOException();
            }
        } else {
            try {
                is = new FileInputStream(source);
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
                logger.error("FileNotFoundException is occured in " + source);
                throw new FileNotFoundException();
            }
        }
        return is;
    }
}
