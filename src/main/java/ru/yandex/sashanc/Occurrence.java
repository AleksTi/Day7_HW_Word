package ru.yandex.sashanc;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 *
 */
public class Occurrence implements IGetOccurences {
    private static final Logger logger = Logger.getLogger(Occurrence.class);
    private ApplicationContext context = new ClassPathXmlApplicationContext("appContext.xml");

    /**
     * Метод создаёт пул потоков и каждому потоку передаёт 1 ресурс и массив слов для поиска в ресурсе
     * @param sources - массив со списком ресурсов
     * @param words - массив со списком слов
     * @param res - файл, куда будут помещены найденные прдложения
     */
    @Override
    public void getOccurences(String[] sources, String[] words, String res) throws WordNotFoundException, SourceNotFoundException {
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
            for (String source : sources) {
                if (source != null) {
                    //Bean "finderCallable" объявлен как scope="prototype", для того чтобы каждому потоку отдавать свой экземпляр
                    Callable<List<String>> finderCallable = (Finder)context.getBean("finderCallable");
                    ((Finder) finderCallable).setSource(source);
                    ((Finder) finderCallable).setWords(words);
                    threadResults.add(service.submit(finderCallable));
                }
            }
            for (Future<List<String>> threadRes : threadResults) {
                listOfListSentences.add(threadRes.get());
            }
        } catch (InterruptedException e) {
            logger.error("InterruptedException is occured", e);
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            logger.error("ExecutionException is occured");
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
        } catch (IOException e) {
            logger.error("IOException is occured");
        }
    }
}
