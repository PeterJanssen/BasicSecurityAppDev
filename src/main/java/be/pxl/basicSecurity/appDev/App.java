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
        String publicKeyFileName = "";
        String privateKeyFileName = "";
        String encryptedAESKeyFileName = "";
        try (Scanner in = new Scanner(System.in)) {
            while (AESFilePath.equals("") || outputFileEncName.equals("") || outputFileDecName.equals("") ||
                    ivByteFileName.equals("") || encryptedHashFileName.equals("")) {
                System.out.println("current path is: " + System.getProperty("user.dir"));
                System.out.print("Give me a path to a file: ");
                AESFilePath = args[0];
                System.out.print("Give me a name for the encrypted file: ");
                outputFileEncName = args[1];
                System.out.print("Give me a name for the decrypted file: ");
                outputFileDecName = args[2];
                System.out.print("Give me a name for the initialization vector bytes: ");
                ivByteFileName = args[3];
                System.out.print("Give me a name for the AES AESKey file: ");
                keyFileName = args[4];
                System.out.print("Give me a name for the original hash file: ");
                originalTestHashFileName = args[5];
                System.out.print("Give me a name for the encrypted hash file: ");
                encryptedHashFileName = args[6];
                System.out.print("Give me a name for the decrypted hash file: ");
                decryptedHashFileName = args[7];
                System.out.print("Give me a name for the RSA public AESKey file: ");
                publicKeyFileName = args[8];
                System.out.print("Give me a name for the RSA private AESKey file: ");
                privateKeyFileName = args[9];
                System.out.print("Give me a name for the encrypted AES AESKey file name: ");
                encryptedAESKeyFileName = args[10];
            }

            Path inputFile = Paths.get(AESFilePath);
            Path outputFileEnc = inputFile.getParent().resolve(outputFileEncName);
            Path outputFileDec = inputFile.getParent().resolve(outputFileDecName);
            Path ivByteFile = inputFile.getParent().resolve(ivByteFileName);
            Path AESKeyFile = inputFile.getParent().resolve(keyFileName);
            Path encryptedHashFile = inputFile.getParent().resolve(encryptedHashFileName);
            Path decryptedHashFile = inputFile.getParent().resolve(decryptedHashFileName);
            Path testHashFile = inputFile.getParent().resolve(originalTestHashFileName);
            Path publicKeyFile = inputFile.getParent().resolve(publicKeyFileName);
            Path privateKeyFile = inputFile.getParent().resolve(privateKeyFileName);
            Path encryptedAESKeyFile = inputFile.getParent().resolve(encryptedAESKeyFileName);

            KeyPair kp = RSAEncryption.getKeyPair(KeySize.SIZE_2048);
            PublicKey publicKey = kp.getPublic();
            PrivateKey privateKey = kp.getPrivate();
            Key AESKey = AESEncryption.generateRandomAESKey(KeySize.SIZE_128);
            IvParameterSpec iv = AESEncryption.generateInitVector();
            IOCrypto.writeKeyToFile(AESKey, AESKeyFile);
            IOCrypto.writeKeyToFile(publicKey, publicKeyFile);
            IOCrypto.writeKeyToFile(privateKey, privateKeyFile);
            IOCrypto.writeInitVectorToFile(iv, ivByteFile);
            AESEncryption.encryptAES(IOCrypto.readKeyFromFile(AESKeyFile, UsableAlgorithm.AES, KeySize.SIZE_128), iv, inputFile, outputFileEnc);
            byte[] encryptedAESKey =  RSAEncryption.encryptSymmetricKeyRSA(publicKey, AESKey, encryptedAESKeyFile);
            IOCrypto.writeEncryptedKeyToFile(encryptedAESKey, encryptedAESKeyFile);
            AESKey = RSAEncryption.decryptSymmetricKeyRSA(privateKey, encryptedAESKeyFile, KeySize.SIZE_2048, UsableAlgorithm.AES);
            AESEncryption.decryptAES(AESKey, IOCrypto.readInitVectorFromFile(ivByteFile), outputFileEnc, outputFileDec);
            String hash = HashSHA256.generateHash(inputFile);
            IOCrypto.writeHashToFile(hash, testHashFile);
            RSAEncryption.encryptHashFileRSA(publicKey, testHashFile, encryptedHashFile);
            RSAEncryption.decryptHashFileRSA(privateKey, encryptedHashFile, decryptedHashFile);
            if (IOCrypto.readHashFromFile(testHashFile).equals(IOCrypto.readHashFromFile(decryptedHashFile))) {
                System.out.println("De hashes waren hetzelfde!");
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
