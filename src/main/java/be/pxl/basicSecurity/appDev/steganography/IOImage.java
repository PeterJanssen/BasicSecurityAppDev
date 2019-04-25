package be.pxl.basicSecurity.appDev.steganography;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class IOImage {
    public static BufferedImage readImage(Path pathToImage) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(pathToImage.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public static void writeImage(BufferedImage image, Path pathToWrite) {
        try {
            ImageIO.write(image, "png", pathToWrite.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage userSpace(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = newImage.createGraphics();
        graphics.drawRenderedImage(image, null);
        graphics.dispose();

        return newImage;
    }
/*
    public static BufferedImage writeTextToImage(BufferedImage image, String text) {
    }

    public static String readTextFromImage(BufferedImage image) {
    }*/
}
