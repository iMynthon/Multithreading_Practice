import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@RequiredArgsConstructor
public class CompressionImageOfThread extends Thread {

    private final File[] files;

    private final String DST_FOLDER;

    private final Integer newWidth;

    private final int numberOfThread;

    @Override
    public void run() {

        System.out.println("Поток " + numberOfThread + " запущен");

        for (File file : files) {
            try {
                BufferedImage image = ImageIO.read(file);
                BufferedImage compression = Scalr.resize(image, 900);
                File newFile = new File(DST_FOLDER + " " + file.getName());
                ImageIO.write(compression, "jpg", newFile);
            } catch (Exception _) {

            }
        }

        System.out.println("Поток " + numberOfThread + " закончил работу.");

    }
}
