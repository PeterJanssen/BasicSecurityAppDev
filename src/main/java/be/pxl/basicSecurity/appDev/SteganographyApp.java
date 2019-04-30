package be.pxl.basicSecurity.appDev;

import be.pxl.basicSecurity.appDev.crypto.AESEncryption;
import be.pxl.basicSecurity.appDev.crypto.KeySize;
import be.pxl.basicSecurity.appDev.steganography.SteganoASCII;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class SteganographyApp {
    public static void main(String[] args) {
        String originalText = "Dit is een testtekst.";
        boolean[] bits = SteganoASCII.encode(originalText);
        String decodedText = SteganoASCII.decode(bits);
        System.out.println(decodedText);

        try {
            Key key = AESEncryption.generateRandomAESKey(KeySize.SIZE_128);
            IvParameterSpec iv = AESEncryption.generateInitVector();

            byte[] bytes = AESEncryption.encryptStringAES(originalText, key, iv);
            String decryptedText = AESEncryption.decryptStringAES(bytes, key, iv);
            System.out.println(decodedText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException
                | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}
