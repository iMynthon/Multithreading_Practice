import java.io.File;

public class Main {

    private final static String SRC_FOLDER = "C:/Users/Mynthon/Pictures/Баскет/Домашние фото";

    private final static String DST_FOLDER = "C:/Users/Mynthon/Pictures/Сжатые изображения/newImage";

    private static final File SRC_DIR = new File(SRC_FOLDER);

    private static final Integer CORES = Runtime.getRuntime().availableProcessors();

    private static int numberOfThread = 1;

    public static void main(String[] args) {

        File[] images = SRC_DIR.listFiles();
        if(images == null){
            System.out.println("Изображения не найдены");
            System.exit(0);
        }

        int filesPerThread = (int) Math.ceil((double) images.length / CORES);


        for (int i = 0; i < CORES; i++) {
            int startIdx = i * filesPerThread;
            int endIdx = Math.min(startIdx + filesPerThread, images.length);

            if (startIdx >= endIdx) {
                break;
            }

            File[] filesPart = new File[endIdx - startIdx];

            System.arraycopy(images, startIdx, filesPart, 0, endIdx - startIdx);

            CompressionImageOfThread resizer = new CompressionImageOfThread(filesPart, DST_FOLDER,800,numberOfThread++);
            resizer.start();
        }
    }


}
