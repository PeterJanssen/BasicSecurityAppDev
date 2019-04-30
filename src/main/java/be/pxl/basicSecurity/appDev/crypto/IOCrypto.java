package be.pxl.basicSecurity.appDev.crypto;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class IOCrypto {

    private IOCrypto() {

    }

    public static Key readKeyFromFile(Path path, UsableAlgorithm algorithm, KeySize keySize) throws IOException {
        byte[] bytes = new byte[keySize.getLength() / 8];
        try (FileInputStream stream = new FileInputStream(path.toFile())) {
            stream.read(bytes);
            return new SecretKeySpec(bytes, 0, bytes.length, algorithm.toString());
        }
    }

    public static void writeKeyToFile(Key key, Path path) throws IOException {
        try (FileOutputStream stream = new FileOutputStream(path.toFile())) {
            stream.write(key.getEncoded());
        }
    }

    public static void writeEncryptedKeyToFile(byte[] bytes, Path path) throws IOException {
        try (FileOutputStream stream = new FileOutputStream(path.toFile())) {
            stream.write(bytes);
        }
    }

    /**
     * Gets a an AES CBC initialization vector, based on a locally stored file containing the bytes to create
     * the vector with
     *
     * @param byteFile The path to the file, containing the bytes to create the initialization vector
     * @return an instance of IvParameterSpec, based on a file containing 16 bytes of data
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */

    public static IvParameterSpec readInitVectorFromFile(Path byteFile) throws IOException {
        try (DataInputStream stream = new DataInputStream(new FileInputStream(byteFile.toFile()))) {
            byte[] bytes = new byte[16];
            stream.readFully(bytes);
            return new IvParameterSpec(bytes);
        }
    }

    public static void writeInitVectorToFile(IvParameterSpec iv, Path path) throws IOException {
        try (FileOutputStream stream = new FileOutputStream(path.toFile())) {
            stream.write(iv.getIV());
        }
    }

    /**
     * Reads a supposed hash from a specified file
     *
     * @param path the path to the file from which the hash is read
     * @return A string representing the SHA-256 hash
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */

    public static String readFile(Path path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            return sb.toString();
        }
    }

    public static String readHashFromFile(Path path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            return reader.readLine();
        }
    }

    public static void writeToFile(String hash, Path path) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
            writer.write(hash);
        }
    }

    public static PublicKey CreatePublicKeyFromFile(Path path) throws Exception {
        byte[] keyBytes = Files.readAllBytes(path);
        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static PrivateKey CreatePrivateKeyFromFile(Path path) throws Exception {
        byte[] keyBytes = Files.readAllBytes(path);
        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
}
