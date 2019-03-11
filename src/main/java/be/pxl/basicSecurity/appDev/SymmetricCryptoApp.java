package be.pxl.basicSecurity.appDev;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

/**
 * Application class that encrypts and decrypts a specified file, using AES
 * and 3DES symmetric encryption algorithms
 */

// TODO write doc for this class
public class SymmetricCryptoApp {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            String filePath = "";
            String outputFileEncName = "";
            String outputFileDecName = "";
            while (filePath.equals("") || outputFileEncName.equals("")) {
                System.out.print("Give me a path to a file: ");
                filePath = in.nextLine();
                System.out.print("Give me a name for the encrypted file: ");
                outputFileEncName = in.nextLine();
                System.out.println("Give me a name for the decrypted file: ");
                outputFileDecName = in.nextLine();

            }

            Path inputFile = Paths.get(filePath);
            Path outputFileEnc = inputFile.getParent().resolve(outputFileEncName);
            Path outputFileDec = inputFile.getParent().resolve(outputFileDecName);
            SecretKey key = generateRandomAESKey(AESKeySize.SIZE_128);
            IvParameterSpec iv = generateInitVector();
            encryptAES(key, iv, inputFile, outputFileEnc);
            decryptAES(key, iv, outputFileEnc, outputFileDec);

            System.out.println();
            System.out.println("Encrypted text AES: " /*+ encryptAES()*/); //TODO uncoomment method call when implemented
            System.out.println("Decrpyted text AES:" /*+ decryptAES()*/); //TODO uncoomment method call when implemented
            System.out.println("Encrypted text 3DES: " /*+ encrypt3DES()*/); //TODO uncoomment method call when implemented
            System.out.println("Decrpyted text 3DES: " /*+ decrypt3DES()*/); //TODO uncoomment method call when implemented
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Decrypt a file, specified by a given path
     *
     * @param key        The AES key being used
     * @param initVector The initialization vector being used
     * @param inputFile  Specifies the path to the file to decrypt
     * @param outputFile Specifies the path to the decrypted file
     * @throws NoSuchAlgorithmException           When a particular cryptographic algorithm is requested
     *                                            but is not available in the environment
     * @throws NoSuchPaddingException             When the specified padding pattern isn't available in the
     *                                            environment
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */

    //TODO Refactor duplicate code in encryptAES() and decryptAES()
    private static void decryptAES(SecretKey key, IvParameterSpec initVector, Path inputFile, Path outputFile) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        if (!key.getAlgorithm().equals("AES")) {
            throw new NoSuchAlgorithmException();
        }
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key, initVector);

        try (FileInputStream fileInputStream = new FileInputStream(inputFile.toString());
             FileOutputStream fileOutputStream = new FileOutputStream(outputFile.toString())) {
            byte[] inputBytes = new byte[(int) inputFile.toFile().length()];
            fileInputStream.read(inputBytes);
            byte[] outputBytes = c.doFinal(inputBytes);
            fileOutputStream.write(outputBytes);
        }
    }

    /**
     * Encrypt a file in AES, utilizing cipher block chaining and PKCS5 padding specification
     *
     * @param key        The AES key being used
     * @param initVector The initialization vector being used
     * @param inputFile  Specifies the path to the file to encrypt
     * @param outputFile Specifies the path to the location of the encrypted file
     * @throws NoSuchAlgorithmException           When a particular cryptographic algorithm is requested
     *                                            but is not available in the environment
     * @throws NoSuchPaddingException             When the specified padding pattern isn't available in the
     *                                            environment
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */

    private static void encryptAES(SecretKey key, IvParameterSpec initVector, Path inputFile, Path outputFile) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        if (!key.getAlgorithm().equals("AES")) {
            throw new NoSuchAlgorithmException();
        }
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key, initVector);

        try (FileInputStream fileInputStream = new FileInputStream(inputFile.toString());
             FileOutputStream fileOutputStream = new FileOutputStream(outputFile.toString())) {
            byte[] inputBytes = new byte[(int) inputFile.toFile().length()];
            fileInputStream.read(inputBytes);
            byte[] outputBytes = c.doFinal(inputBytes);
            fileOutputStream.write(outputBytes);
        }
    }

    /**
     * Generates a random AES key
     *
     * @param length Length of the key in bits, specified by an instance of enum AESKeySize
     * @throws NoSuchAlgorithmException When a particular cryptographic algorithm is requested
     *                                  but is not available in the environment
     */

    private static SecretKey generateRandomAESKey(AESKeySize length) throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(length.getLength(), new SecureRandom());
        return kg.generateKey();
    }

    /**
     * Generates a random initialization vector for the initial AES CBC cipher block
     * for block size = 16 bytes
     */

    private static IvParameterSpec generateInitVector() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return new IvParameterSpec(bytes);
    }

    // decrypt 3DES

    // TODO Implement method
    private static String decrypt3DES() {
        throw new NotImplementedException();
    }

    // Encrypt 3DES

    // TODO Implement method
    private static String encrypt3DES() {
        throw new NotImplementedException();
    }
}
