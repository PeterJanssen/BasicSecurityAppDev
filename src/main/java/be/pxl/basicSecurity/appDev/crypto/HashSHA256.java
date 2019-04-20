package be.pxl.basicSecurity.appDev.crypto;

import java.io.*;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This utility class generates a SHA-256 hash
 */

public class HashSHA256 {

    private HashSHA256() {
    }

    /**
     * Generates a SHA-256 hash from a specified file,
     * and writes it to another specified file
     *
     * @param inputFile  The path to file of which a hash is generated
     * @param outputFile The path to the file to which the has is written
     * @throws NoSuchAlgorithmException When a particular cryptographic algorithm is requested
     *                                  but is not available in the environment.
     * @throws IOException              Signals that an I/O exception of some sort has occurred.
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile.toFile()))) {
            writer.write(result.toString());
        }
    }

    /**
     * Reads a SHA-256 hash from a specified file
     *
     * @param file the path to the file from which the hash is read
     * @return A string representing the SHA-256 hash
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */

    public static String readHash(Path file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            return reader.readLine();
        }
    }
}
