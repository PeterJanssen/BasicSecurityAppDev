package be.pxl.basicSecurity.appDev;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

// TODO Create enum for bit length of key (only 128, 192 and 256 bit length allowed)

public class SymmetricCryptoApp {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Give me a path to a file: ");
            String filePath = in.nextLine();
            System.out.println();
            System.out.println("Encrypted text AES: " /*+ encryptAES()*/); //TODO uncoomment method call when implemented
            System.out.println("Decrpyted text AES:" /*+ decryptAES()*/); //TODO uncoomment method call when implemented
            System.out.println("Encrypted text 3DES: " /*+ encrypt3DES()*/); //TODO uncoomment method call when implemented
            System.out.println("Decrpyted text 3DES: " /*+ decrypt3DES()*/); //TODO uncoomment method call when implemented
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Decrypt AES

    // TODO Implement method
    private static String decryptAES(String key, String initVector, String cipherText) {
        throw new NotImplementedException();
    }

    // Encrypt AES

    // TODO Implement method
    private static String encryptAES(String key, String initVector, String plain) {
        throw new NotImplementedException();
    }

    /**
     * Generates a random AES key
     *
     * @param length Length of the key in bits
     * @throws NoSuchAlgorithmException When a particular cryptographic algorithm is requested
     *                                  but is not available in the environment
     */

    private static SecretKey generateRandomAESKey(int length) throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(length);
        return kg.generateKey();
    }

    /**
     * Generates a random initialization vector for the initial AES CBC cipher block
     *
     * @param length The length of the AES key in bits. Used to generate an
     *               initialization vector of equivalent length
     * @throws
     */
    // TODO Implement method
    private static IvParameterSpec generateInitVector(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length / 8];
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
