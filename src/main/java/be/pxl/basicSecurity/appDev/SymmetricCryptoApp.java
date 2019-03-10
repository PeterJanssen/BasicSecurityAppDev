package be.pxl.basicSecurity.appDev;

import java.util.Scanner;

public class SymmetricCryptoApp {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Give me a path to a file: ");
            String filePath = in.next();
            System.out.println();
            System.out.println("Encrypted text AES: " + encryptAES());
            System.out.println("Decrpyted text AES:" + decryptAES());
            System.out.println("Encrypted text 3DES: "+ encrypt3DES());
            System.out.println("Decrpyted text 3DES: "+ decrypt3DES());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Decrypt AES

    private static String decryptAES(String key, String initVector, String cipherText) {
        return null;
    }

    // Encrypt AES

    private static String encryptAES(String key, String initVector, String plain) {
        return null;
    }

    // decrypt 3DES

    private static String decrypt3DES() {
        return null;
    }

    // Encrypt 3DES

    private static String encrypt3DES() {
        return null;
    }
}
