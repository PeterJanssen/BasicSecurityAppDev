package be.pxl.basicSecurity.appDev;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.Scanner;

public class MessageDigestApp {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter your username");
        String username = in.nextLine();
        System.out.println("Enter your password:");
        String password = in.nextLine();
        StringBuffer hexString = getHexStringSHA256(password);
        System.out.println("Hex format: " + hexString.toString());
    }

    private static StringBuffer getHexStringSHA256(String message) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        sha256.update(message.getBytes());
        byte[] digest = sha256.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            hexString.append(Integer.toHexString(0xFF & digest[i]));
        }

        return hexString;
    }
}
