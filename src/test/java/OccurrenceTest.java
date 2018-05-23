import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.sashanc.Occurrence;
import ru.yandex.sashanc.SourceNotFoundException;
import ru.yandex.sashanc.WordNotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class OccurrenceTest {
    private static Logger logger = Logger.getLogger(OccurrenceTest.class);

    Occurrence oc;
    String[] sourceList;
    String[] wordList;
    String fileRes;

    @BeforeClass
    public static void beforeTests(){
        logger.info("@BeforeClass");
    }

    @Before
    public void before(){
        logger.info("@Before");

        this.oc = new Occurrence();
        sourceList = new String[2000];
        wordList = new String[200];
        fileRes = "E://work/res.txt";
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
    }

    @Test
    public void getOccurencesTest() throws SourceNotFoundException, IOException, WordNotFoundException {
        this.oc.getOccurences(sourceList, wordList, fileRes);
    }

    @Test(expected = SourceNotFoundException.class)
    public void getOccurencesTestWSNFException() throws SourceNotFoundException, IOException, WordNotFoundException {
        sourceList = null;
        this.oc.getOccurences(sourceList, wordList, fileRes);
    }


    @Test(expected = WordNotFoundException.class)
    public void getOccurencesTestWWNFException() throws SourceNotFoundException, IOException, WordNotFoundException {
        wordList = null;
        this.oc.getOccurences(sourceList, wordList, fileRes);
    }
}
