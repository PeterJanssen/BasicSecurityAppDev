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
            String ivByteFileName = "";
            while (filePath.equals("") || outputFileEncName.equals("")) {
                System.out.print("Give me a path to a file: ");
                filePath = in.nextLine();
                System.out.print("Give me a name for the encrypted file: ");
                outputFileEncName = in.nextLine();
                System.out.print("Give me a name for the decrypted file: ");
                outputFileDecName = in.nextLine();
                System.out.print("Give me a name for the IV bytefile: ");
                ivByteFileName = in.nextLine();

            }

            Path inputFile = Paths.get(filePath);
            Path outputFileEnc = inputFile.getParent().resolve(outputFileEncName);
            Path outputFileDec = inputFile.getParent().resolve(outputFileDecName);
            Path ivByteFile = inputFile.getParent().resolve(ivByteFileName);
            SecretKey key = generateRandomAESKey(AESKeySize.SIZE_128);
            IvParameterSpec iv = generateInitVector(ivByteFile);
            encryptAES(key, iv, inputFile, outputFileEnc);
            decryptAES(key, getInitVectorAES(ivByteFile), outputFileEnc, outputFileDec);

            System.out.println();
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

        runAlgorithm(inputFile, outputFile, c);
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

    private static void runAlgorithm(Path inputFile, Path outputFile, Cipher c) throws IOException, IllegalBlockSizeException, BadPaddingException {
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
     *
     * @param path Path to the folder where the bytestream will be stored
     */

    private static IvParameterSpec generateInitVector(Path path) throws IOException {
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

    private static IvParameterSpec getInitVectorAES(Path byteFile) throws IOException {
        try (DataInputStream stream = new DataInputStream(new FileInputStream(byteFile.toFile()))) {
            byte[] bytes = new byte[16];
            stream.readFully(bytes);
            return new IvParameterSpec(bytes);
        }
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
