import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.concurrent.ForkJoinPool;

public class Main {


    public static final String ELEMENTARY_URL = "https://sendel.ru/";

    public static void main(String[] args) {

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        Links links = forkJoinPool.invoke(new MappingSite(ELEMENTARY_URL));

        try {
            File file = new File("C:\\Users\\Mynthon\\IdeaProjects\\java_basics\\Multithreading\\ForkJoinPool\\data\\result.txt");
            file.getParentFile().mkdir();
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            recordLinks(links, printWriter, 0);

            printWriter.flush();
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(links.getChildLinks());
    }

    public static void recordLinks(Links links, PrintWriter printWriter, int depth) {
        System.out.println("Depth: " + depth + ", url: " + links.getUrl());

        printWriter.println("\t".repeat(depth) + links.getUrl());

        links.getChildLinks().sort(Comparator.comparing(Links::getUrl));

        for (Links link : links.getChildLinks()) {
            recordLinks(link, printWriter, depth + 1);
        }
    }
}
