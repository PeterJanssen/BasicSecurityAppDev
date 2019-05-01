package be.pxl.basicSecurity.appDev;

import be.pxl.basicSecurity.appDev.crypto.AESEncryption;
import be.pxl.basicSecurity.appDev.crypto.KeySize;
import be.pxl.basicSecurity.appDev.crypto.RSAEncryption;
import be.pxl.basicSecurity.appDev.steganography.LSB_decode;
import be.pxl.basicSecurity.appDev.steganography.LSB_encode;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.Scanner;

public class SteganoApp {
    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        BufferedImage image = null;
        Path currentDir = Paths.get(System.getProperty("user.dir"));
        Path imagePath;
        Path imageTextPath;
        Path textFilePath;
        String text = "Don't do drugs, kids.";

        IvParameterSpec iv = AESEncryption.generateInitVector();
        Key key =  AESEncryption.generateRandomAESKey(KeySize.SIZE_128);
        String encText = AESEncryption.encryptStringAES(text, key, iv);

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

        try (FileWriter writer = new FileWriter(textFilePath.toFile())) {
            int[] bits = LSB_encode.getMessageBits(encText);
            LSB_encode.hideTheMessage(bits, image, imageTextPath);
            String  decodedText = LSB_decode.DecodeTheMessage(imageTextPath);
            String decryptedText = AESEncryption.decryptStringAES(decodedText, key, iv);
            writer.write(decryptedText);
            System.out.println(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
