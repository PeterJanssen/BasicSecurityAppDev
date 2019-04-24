package be.pxl.basicSecurity.appDev;

import be.pxl.basicSecurity.appDev.crypto.*;

import javax.crypto.spec.IvParameterSpec;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        String AESFilePath = "";
        String outputFileEncName = "";
        String outputFileDecName = "";
        String ivByteFileName = "";
        String keyFileName = "";
        String originalTestHashFileName = "";
        String encryptedHashFileName = "";
        String decryptedHashFileName = "";
        String encrypterPublicKeyFileName = "";
        String encrypterPrivateKeyFileName = "";
        String decrypterPublicKeyFileName = "";
        String decrypterPrivateKeyFileName = "";
        String encryptedAESKeyFileName = "";
        try (Scanner in = new Scanner(System.in)) {
            while (AESFilePath.equals("") || outputFileEncName.equals("") || outputFileDecName.equals("") ||
                    ivByteFileName.equals("") || encryptedHashFileName.equals("")) {
                System.out.println("current path is: " + System.getProperty("user.dir"));
                System.out.print("Give me a path to a file: ");
                AESFilePath = args[0];
                System.out.println(AESFilePath);
                System.out.print("Give me a name for the encrypted file: ");
                outputFileEncName = args[1];
                System.out.println(outputFileEncName);
                System.out.print("Give me a name for the decrypted file: ");
                outputFileDecName = args[2];
                System.out.println(outputFileDecName);
                System.out.print("Give me a name for the initialization vector bytes: ");
                ivByteFileName = args[3];
                System.out.println(ivByteFileName);
                System.out.print("Give me a name for the AES Key file: ");
                keyFileName = args[4];
                System.out.println(keyFileName);
                System.out.print("Give me a name for the original hash file: ");
                originalTestHashFileName = args[5];
                System.out.println(originalTestHashFileName);
                System.out.print("Give me a name for the encrypted hash file: ");
                encryptedHashFileName = args[6];
                System.out.println(encryptedHashFileName);
                System.out.print("Give me a name for the decrypted hash file: ");
                decryptedHashFileName = args[7];
                System.out.println(decryptedHashFileName);
                System.out.print("Give me a name for the encrypter's RSA public Key file: ");
                encrypterPublicKeyFileName = args[8];
                System.out.println(encrypterPublicKeyFileName);
                System.out.print("Give me a name for the  encrypter's RSA private Key file: ");
                encrypterPrivateKeyFileName = args[9];
                System.out.println(encrypterPrivateKeyFileName);
                System.out.print("Give me a name for the decrypter's RSA public Key file: ");
                decrypterPublicKeyFileName = args[10];
                System.out.println(decrypterPublicKeyFileName);
                System.out.print("Give me a name for the decrypter's RSA private Key file: ");
                decrypterPrivateKeyFileName = args[11];
                System.out.println(decrypterPrivateKeyFileName);
                System.out.print("Give me a name for the encrypted AES Key file name: ");
                encryptedAESKeyFileName = args[12];
                System.out.println(encryptedAESKeyFileName);
            }

            Path inputFile = Paths.get(AESFilePath);
            Path outputFileEnc = inputFile.getParent().resolve(outputFileEncName);
            Path outputFileDec = inputFile.getParent().resolve(outputFileDecName);
            Path ivByteFile = inputFile.getParent().resolve(ivByteFileName);
            Path AESKeyFile = inputFile.getParent().resolve(keyFileName);
            Path encryptedHashFile = inputFile.getParent().resolve(encryptedHashFileName);
            Path decryptedHashFile = inputFile.getParent().resolve(decryptedHashFileName);
            Path testHashFile = inputFile.getParent().resolve(originalTestHashFileName);
            Path encrypterPublicKeyFile = inputFile.getParent().resolve(encrypterPublicKeyFileName);
            Path encrypterPrivateKeyFile = inputFile.getParent().resolve(encrypterPrivateKeyFileName);
            Path decrypterPublicKeyFile = inputFile.getParent().resolve(decrypterPublicKeyFileName);
            Path decrypterPrivateKeyFile = inputFile.getParent().resolve(decrypterPrivateKeyFileName);
            Path encryptedAESKeyFile = inputFile.getParent().resolve(encryptedAESKeyFileName);

            KeyPair kpEnc = RSAEncryption.getKeyPair(KeySize.SIZE_2048);
            KeyPair kpDec = RSAEncryption.getKeyPair(KeySize.SIZE_2048);
            PublicKey publicKeyEnc = kpEnc.getPublic();
            PrivateKey privateKeyEnc = kpEnc.getPrivate();
            PublicKey publicKeyDec = kpDec.getPublic();
            PrivateKey privateKeyDec = kpDec.getPrivate();
            Key AESKey = AESEncryption.generateRandomAESKey(KeySize.SIZE_128);
            IvParameterSpec iv = AESEncryption.generateInitVector();
            IOCrypto.writeKeyToFile(AESKey, AESKeyFile);
            IOCrypto.writeKeyToFile(publicKeyEnc, encrypterPublicKeyFile);
            IOCrypto.writeKeyToFile(privateKeyEnc, encrypterPrivateKeyFile);
            IOCrypto.writeKeyToFile(publicKeyDec, decrypterPublicKeyFile);
            IOCrypto.writeKeyToFile(privateKeyDec, decrypterPrivateKeyFile);
            IOCrypto.writeInitVectorToFile(iv, ivByteFile);

            AESEncryption.encryptAES(IOCrypto.readKeyFromFile(AESKeyFile, UsableAlgorithm.AES, KeySize.SIZE_128), iv, inputFile, outputFileEnc);
            byte[] encryptedAESKey = RSAEncryption.encryptSymmetricKeyRSA(publicKeyDec, AESKey, encryptedAESKeyFile);
            IOCrypto.writeEncryptedKeyToFile(encryptedAESKey, encryptedAESKeyFile);
            AESKey = RSAEncryption.decryptSymmetricKeyRSA(privateKeyDec, encryptedAESKeyFile, KeySize.SIZE_2048, UsableAlgorithm.AES);
            AESEncryption.decryptAES(AESKey, IOCrypto.readInitVectorFromFile(ivByteFile), outputFileEnc, outputFileDec);
            String hash = HashSHA256.generateHash(inputFile);
            IOCrypto.writeHashToFile(hash, testHashFile);
            RSAEncryption.encryptHashFileRSA(privateKeyEnc, testHashFile, encryptedHashFile);
            RSAEncryption.decryptHashFileRSA(publicKeyEnc, encryptedHashFile, decryptedHashFile);
            if (IOCrypto.readHashFromFile(testHashFile).equals(IOCrypto.readHashFromFile(decryptedHashFile))) {
                System.out.println("De hashes waren hetzelfde!");
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
