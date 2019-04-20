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
     *
     * @param inputFile  The path to file of which a hash is generated
     * @throws NoSuchAlgorithmException When a particular cryptographic algorithm is requested
     *                                  but is not available in the environment.
     * @throws IOException              Signals that an I/O exception of some sort has occurred.
     */

    public static String generateHash(Path inputFile) throws NoSuchAlgorithmException, IOException {
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

        return result.toString();
    }
}
