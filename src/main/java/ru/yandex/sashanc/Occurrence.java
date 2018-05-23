package ru.yandex.sashanc;

import org.apache.log4j.Logger;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 *
 */
public class Occurrence implements IGetOccurences {
    final static Logger logger = Logger.getLogger(Occurrence.class);

    /**
     * Метод создаёт пул потоков и каждому потоку передаёт 1 ресурс и массив слов для поиска в ресурсе
     * @param sources
     * @param words
     * @param res
     */
    @Override
    public void getOccurences(String[] sources, String[] words, String res) throws IOException, WordNotFoundException, SourceNotFoundException {
        logger.info("getOccurences is launched");
        if (sources == null){
            logger.error("SourceNotFoundException is occured");
            throw new SourceNotFoundException("Массив ресурсов не инициализирован");
        }
        if(words == null) {
            logger.error("WordNotFoundException is occured");
            throw new WordNotFoundException("Массив слов не инициализирован");
        }
        List<Future<List<String>>> threadResults = new ArrayList<>();
        List<List<String>> listOfListSentences = new ArrayList<>();
        final ExecutorService service = Executors.newFixedThreadPool(4);
        try {
            for (int i = 0; i < sources.length; i++) {
                if (sources[i] != null) {
                    Callable<List<String>> finderCallable = new Finder(sources[i], words);
                    threadResults.add(service.submit(finderCallable));
                }
            }
            for(Future<List<String>> threadRes : threadResults){
                listOfListSentences.add(threadRes.get());
            }
        } catch (InterruptedException e) {
            logger.error("InterruptedException is occured");
            //e.printStackTrace();
        } catch (ExecutionException e) {
            logger.error("ExecutionException is occured");
            //e.printStackTrace();
        } finally {
            service.shutdown();
        }
        writeSentence(listOfListSentences, res);
        logger.info("getOccurences is finished");
    }


    /**
     * Метод сохраняет в файл предлжения из колллекций полученные от каждого потока,
     * которые в свою очередь хранятся также в коллекции
     * @param listOfListSentences - коллекция предложений, которые содержат слова, заданные для опоиска
     * @param fileRes - имя файла, в который осуществляется запись найденных предложений
     */
    private void writeSentence(List<List<String>> listOfListSentences, String fileRes){
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(fileRes, true)))) {
            for(List<String> stringList : listOfListSentences){
                for(String str : stringList) {
                    writer.write(str);
                    writer.newLine();
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException is occured");
            //e.printStackTrace();
        } catch (IOException e) {
            logger.error("IOException is occured");
            //e.printStackTrace();
        }
    }
}
