package be.pxl.basicSecurity.appDev.crypto;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.file.Path;
import java.security.*;

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

    public static void decryptFileAES(Key key, IvParameterSpec initVector, Path inputFile, Path outputFile) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        if (!key.getAlgorithm().equals(UsableAlgorithm.AES.getAlgorithm())) {
            throw new NoSuchAlgorithmException("You should specify an AES key.");
        }
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key, initVector);

        runAlgorithmFile(inputFile, outputFile, c);
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

    public static void encryptFileAES(Key key, IvParameterSpec initVector, Path inputFile, Path outputFile)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        if (!key.getAlgorithm().equals(UsableAlgorithm.AES.getAlgorithm())) {
            throw new NoSuchAlgorithmException("You should specify an AES key.");
        }
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key, initVector);

        runAlgorithmFile(inputFile, outputFile, c);
    }

    public static byte[] encryptStringAES(String message, Key key, IvParameterSpec initVector)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if (!key.getAlgorithm().equals(UsableAlgorithm.AES.getAlgorithm())) {
            throw new NoSuchAlgorithmException("You should specify an AES key.");
        }
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key, initVector);

        return c.doFinal(message.getBytes());
    }

    public static String decryptStringAES(byte[] bytes, Key key, IvParameterSpec initVector)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key, initVector);

        return c.doFinal(bytes).toString();
    }

    /**
     * Runs the specified symmetric algorithm, using the specified files for input and output,
     * and the specified cipher for the algorithm to be used
     *
     * @param inputFile  The path to the file, containing the input of the algorithm
     * @param outputFile The path to the file, containing the output of the algorithm
     * @param c          The cipher, specifying the used algorithm
     * @throws IOException               Signals that an I/O exception of some sort has occurred.
     * @throws BadPaddingException       Signals that an invalid padding standard has been passed.
     * @throws IllegalBlockSizeException Signals an inappropriate block size being requested.
     */

    private static void runAlgorithmFile(Path inputFile, Path outputFile, Cipher c) throws IOException, IllegalBlockSizeException, BadPaddingException {
        try (FileInputStream fileInputStream = new FileInputStream(inputFile.toString());
             FileOutputStream fileOutputStream = new FileOutputStream(outputFile.toString())) {
            byte[] inputBytes = new byte[(int) inputFile.toFile().length()];
            fileInputStream.read(inputBytes);
            fileOutputStream.write(c.doFinal(inputBytes));
        }
    }

    /**
     * Generates a random AES key
     *
     * @param length Length of the key in bits, specified by an instance of enum AESKeySize
     * @return A randomly generated AES key of specified length
     * @throws NoSuchAlgorithmException When a particular cryptographic algorithm is requested
     *                                  but is not available in the environment
     */

    public static Key generateRandomAESKey(KeySize length) throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(UsableAlgorithm.AES.getAlgorithm());
        kg.init(length.getLength(), new SecureRandom());
        return kg.generateKey();
    }

    /**
     * Generates a random initialization vector for the initial AES CBC cipher block
     * for block size = 16 bytes
     *
     * @return a 128 bit length initialization vector
     */

    public static IvParameterSpec generateInitVector() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return new IvParameterSpec(bytes);
    }
}
