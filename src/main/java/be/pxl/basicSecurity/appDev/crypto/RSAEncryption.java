package be.pxl.basicSecurity.appDev.crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Path;
import java.security.*;

public class RSAEncryption {

    private RSAEncryption() {
    }

    public static KeyPair getKeyPair(KeySize keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(UsableAlgorithm.RSA.getAlgorithm());
        kpg.initialize(keySize.getLength());

        return kpg.generateKeyPair();
    }

    public static byte[] encryptSymmetricKeyRSA(PublicKey asymmetricKey, Key symmetricKey, Path encryptedKeyFilePath)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, IOException {
        Cipher c = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        c.init(Cipher.ENCRYPT_MODE, asymmetricKey);
        byte[] bytes = c.doFinal(symmetricKey.getEncoded());

        return bytes;
    }

    public static SecretKey decryptSymmetricKeyRSA(PrivateKey asymmetricKey, Path encryptedKey,
                                                   KeySize asymmetricKeySize, UsableAlgorithm algorithm)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException,
            BadPaddingException, IllegalBlockSizeException {
        Cipher c = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        c.init(Cipher.DECRYPT_MODE, asymmetricKey);
        byte[] bytes = new byte[asymmetricKeySize.getLength() / 8];
        try (FileInputStream fileInputStream = new FileInputStream(encryptedKey.toFile())) {
            fileInputStream.read(bytes);
        }
        byte[] decryptedBytes = c.doFinal(bytes);

        return new SecretKeySpec(decryptedBytes, 0, decryptedBytes.length, algorithm.getAlgorithm());
    }

    public static void encryptHashFileRSA(PrivateKey key, Path inputFilePath, Path outputFilePath) throws IOException,
            NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException,
            InvalidKeyException {
        try (FileInputStream fileInputStream = new FileInputStream(inputFilePath.toString());
             FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath.toString())) {
            byte[] inputBytes = new byte[(int) inputFilePath.toFile().length()];
            fileInputStream.read(inputBytes);
            Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c.init(Cipher.ENCRYPT_MODE, key);
            fileOutputStream.write(c.doFinal(inputBytes));
        }
    }


    public static void decryptHashFileRSA(PublicKey key, Path inputFilePath, Path outputFilePath) throws IOException,
            NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException,
            InvalidKeyException {
        try (FileInputStream fileInputStream = new FileInputStream(inputFilePath.toString());
             FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath.toString())) {
            byte[] inputBytes = new byte[(int) inputFilePath.toFile().length()];
            fileInputStream.read(inputBytes);
            Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c.init(Cipher.DECRYPT_MODE, key);
            fileOutputStream.write(c.doFinal(inputBytes));
        }
    }
}
