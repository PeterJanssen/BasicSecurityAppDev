package be.pxl.basicSecurity.appDev.steganography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class SteganoApp {
    public static void main(String[] args) {
        BufferedImage image = null;
        Path currentDir = Paths.get(System.getProperty("user.dir"));
        Path imagePath;
        Path imageTextPath;
        Path textFilePath;
        String text = "Don't do drugs, kids.";

        try (Scanner in = new Scanner(System.in)) {
            System.out.println("Current folder is "+ System.getProperty("user.dir"));
            System.out.print("Please enter the path to the image: ");
            imagePath = currentDir.resolve(in.nextLine());
            System.out.print("Please enter the name of the image containing the message: ");
            imageTextPath = imagePath.getParent().resolve(in.nextLine());
            System.out.print("Please enter the name of the file containing the decoded message: ");
            textFilePath = imagePath.getParent().resolve(in.nextLine());
        }

        try {
            image = ImageIO.read(imagePath.toFile());
        } catch (IOException e) {
            System.out.println("Could not find the image for "+ imagePath);
            e.printStackTrace();
        }

        int[] bits = LSB_encode.getMessageBits(text);
        try {
            LSB_encode.hideTheMessage(bits, image, imageTextPath);
            LSB_decode.DecodeTheMessage(imageTextPath, textFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
