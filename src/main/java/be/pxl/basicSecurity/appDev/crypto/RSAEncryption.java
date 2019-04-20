package be.pxl.basicSecurity.appDev.crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.*;

public class RSAEncryption {

    private RSAEncryption() {
    }

    public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(UsableAlgorithm.RSA.getAlgorithm());
        kpg.initialize(2048);

        return kpg.generateKeyPair();
    }

    public static byte[] encryptSymmetricKeyRSA(PublicKey asymmetricKey, SecretKey symmetricKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException {
        Cipher c = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        c.init(Cipher.ENCRYPT_MODE, asymmetricKey);

        return c.doFinal(symmetricKey.getEncoded());
    }

    public static SecretKey decryptSymmetricKeyRSA(PrivateKey asymmetricKey, Path encryptedKey, Path decryptedKey,
                                                   KeySize keySize, UsableAlgorithm algorithm)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException,
            BadPaddingException, IllegalBlockSizeException {
        Cipher c = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        c.init(Cipher.DECRYPT_MODE, asymmetricKey);
        byte[] bytes = new byte[keySize.getLength()];
        try (FileInputStream fileInputStream = new FileInputStream(encryptedKey.toFile())) {
            fileInputStream.read(bytes);
        }
        byte[] decryptedBytes = c.doFinal(bytes);
        try (FileOutputStream fileOutputStream = new FileOutputStream(decryptedKey.toFile())) {
            fileOutputStream.write(decryptedBytes);
        }

        return new SecretKeySpec(decryptedBytes, 0, decryptedBytes.length, algorithm.getAlgorithm());
    }
}
