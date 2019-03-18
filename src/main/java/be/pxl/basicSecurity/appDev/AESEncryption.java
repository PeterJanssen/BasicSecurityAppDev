package be.pxl.basicSecurity.appDev;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

// TODO write doc for this class

/**
 * This utility class offers services to encrypt and decrypt a specified file, using the AES symmetric encryption algorithm,
 * utilizing cipher block chaining and the PKCS#5 (interpreted as PKCS#7 internally) padding standard.
 */

public class AESEncryption {

    private AESEncryption() {
    }

    /**
     * Decrypts a file, specified by a given path.
     *
     * @param key        The AES key being used.
     * @param initVector The initialization vector being used.
     * @param inputFile  Specifies the path to the file to decrypt.
     * @param outputFile Specifies the path to the decrypted file.
     * @throws NoSuchAlgorithmException           When a particular cryptographic algorithm is requested
     *                                            but is not available in the environment.
     * @throws NoSuchPaddingException             When the specified padding pattern isn't available in the
     *                                            environment.
     * @throws InvalidAlgorithmParameterException This is the exception for invalid or inappropriate algorithm
     *                                            parameters.
     * @throws InvalidKeyException                This is the exception for invalid keys.
     * @throws IOException                        Signals that an I/O exception of some sort has occurred.
     * @throws BadPaddingException                Signals that an invalid padding standard has been passed.
     * @throws IllegalBlockSizeException          Signals an inappropriate block size being requested.
     */

    public static void decryptAES(SecretKey key, IvParameterSpec initVector, Path inputFile, Path outputFile) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        if (!key.getAlgorithm().equals("AES")) {
            throw new NoSuchAlgorithmException();
        }
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key, initVector);

        runAlgorithm(inputFile, outputFile, c);
    }

    /**
     * Encrypts a file in AES, utilizing cipher block chaining and PKCS#5 (= PKCS#7 internally) padding specification
     *
     * @param key        The AES key being used
     * @param initVector The initialization vector being used
     * @param inputFile  Specifies the path to the file to encrypt
     * @param outputFile Specifies the path to the location of the encrypted file
     * @throws NoSuchAlgorithmException           When a particular cryptographic algorithm is requested
     *                                            but is not available in the environment
     * @throws NoSuchPaddingException             When the specified padding pattern isn't available in the
     *                                            environment
     * @throws InvalidAlgorithmParameterException This is the exception for invalid or inappropriate algorithm
     *                                            parameters.
     * @throws InvalidKeyException                This is the exception for invalid keys.
     * @throws IOException                        Signals that an I/O exception of some sort has occurred.
     * @throws BadPaddingException                Signals that an invalid padding standard has been passed.
     * @throws IllegalBlockSizeException          Signals an inappropriate block size being requested.
     */

    public static void encryptAES(SecretKey key, IvParameterSpec initVector, Path inputFile, Path outputFile) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        if (!key.getAlgorithm().equals("AES")) {
            throw new NoSuchAlgorithmException();
        }
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key, initVector);

        runAlgorithm(inputFile, outputFile, c);
    }

    /**
     * Runs the specified symmetric algorithm, using the specified files for input and output,
     * and the specified cipher for the algorithm to be used
     *
     * @param inputFile  The path to the file, containing the input of the algorithm
     * @param outputFile The path to the file, containing the output of the algorithm
     * @param c          The cipher, specifying the used algorithm
     */

    public static void runAlgorithm(Path inputFile, Path outputFile, Cipher c) throws IOException, IllegalBlockSizeException, BadPaddingException {
        try (FileInputStream fileInputStream = new FileInputStream(inputFile.toString());
             FileOutputStream fileOutputStream = new FileOutputStream(outputFile.toString())) {
            byte[] inputBytes = new byte[(int) inputFile.toFile().length()];
            fileInputStream.read(inputBytes);
            byte[] outputBytes = c.doFinal(inputBytes);
            fileOutputStream.write(outputBytes);
        }
    }

    /**
     * Generates a random AES key, and saves that key
     *
     * @param length Length of the key in bits, specified by an instance of enum AESKeySize
     * @throws NoSuchAlgorithmException When a particular cryptographic algorithm is requested
     *                                  but is not available in the environment
     */

    public static SecretKey generateRandomAESKey(AESKeySize length, Path path) throws NoSuchAlgorithmException, IOException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(length.getLength(), new SecureRandom());
        SecretKey key = kg.generateKey();
        byte[] bytes = new byte[length.getLength() / 8];
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            stream.writeObject(key);
        }
        return key;
    }

    /**
     * Generates a random initialization vector for the initial AES CBC cipher block
     * for block size = 16 bytes
     *
     * @param path Path to the folder where the bytestream will be stored
     */

    public static IvParameterSpec generateInitVector(Path path) throws IOException {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        try (DataOutputStream stream = new DataOutputStream(new FileOutputStream(path.toFile()))) {
            random.nextBytes(bytes);
            stream.write(bytes);
        }
        return new IvParameterSpec(bytes);
    }

    /**
     * Gets an AES CBC initialization vector, based on
     *
     * @param byteFile The path to the file, containing the bytes to create the initialization vector
     * @return an instance of IvParameterSpec, based on a file containing 16 bytes of data
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */

    public static IvParameterSpec getInitVectorAES(Path byteFile) throws IOException {
        try (DataInputStream stream = new DataInputStream(new FileInputStream(byteFile.toFile()))) {
            byte[] bytes = new byte[16];
            stream.readFully(bytes);
            return new IvParameterSpec(bytes);
        }
    }

    public static SecretKey getKeyAES(Path keyFile) throws IOException, ClassNotFoundException {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(keyFile.toFile()))) {
            return  (SecretKey) stream.readObject();
        }
    }
}
