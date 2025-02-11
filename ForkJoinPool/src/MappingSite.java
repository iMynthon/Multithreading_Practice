
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveTask;


public class MappingSite extends RecursiveTask<Links> {

    private static String ELEMENTARY_URL;

    private final String another_url;

    private final Queue<String> visitedUrls;

    public MappingSite(String url) {
        this(url, new ConcurrentLinkedQueue<>());
        ELEMENTARY_URL = url;
    }

    public MappingSite(String url, Queue<String> visitedUrls) {
        this.another_url = url;
        this.visitedUrls = visitedUrls;
    }

    @Override
    protected Links compute() {

        Links currentLink = new Links(another_url);

        synchronized (visitedUrls) {
            if (visitedUrls.contains(another_url)) {
                return currentLink;
            }
            visitedUrls.add(another_url);
        }

        System.out.println("Processing url: " + another_url);

        List<MappingSite> mappingTasks = new CopyOnWriteArrayList<>();

        try {

            Document connection = Jsoup.connect(another_url)
                    .ignoreContentType(true)
                    .get();

            Elements elements = connection.select("a");

            for (Element element : elements) {
                String abshref = element.attr("abs:href");
                synchronized (elements) {
                    if (isValidLink(abshref.trim())) {
                        MappingSite mapSite = new MappingSite(abshref, visitedUrls);
                        mapSite.fork();

                        mappingTasks.add(mapSite);

                        Thread.sleep(150);
                    }
                }


            }
            for (MappingSite mappingSite : mappingTasks) {
                Links child = mappingSite.join();
                currentLink.addChildLink(child);
            }

        }catch (IOException | InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        return currentLink;
    }

    private boolean isValidLink(String url) {
        synchronized (visitedUrls) {
            return url.startsWith(ELEMENTARY_URL) && !url.contains("#") && !visitedUrls.contains(url);
        }
    }
}
