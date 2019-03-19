package be.pxl.basicSecurity.appDev;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Generates a SHA-256 hash
 */

public class HashSHA256 {

    private HashSHA256() {
    }

    /**
     * Generates a SHA-256 hash, and writes it to
     *
     * @param inputFile  The path to file of which a hash is generated
     * @param outputFile The path to the file to which the has is written
     */

    public static void generateHash(Path inputFile, Path outputFile) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try (DigestInputStream dis = new DigestInputStream(
                new FileInputStream(inputFile.toFile()), md)
        ) {
            while (dis.read() != -1) {
                md = dis.getMessageDigest();
            }
        }

        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        System.out.println(result.toString());

    }


    public static String readHash(Path file) {
        throw new NotImplementedException();
    }
}
