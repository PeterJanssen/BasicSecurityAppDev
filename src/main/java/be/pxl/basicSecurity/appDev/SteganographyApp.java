package be.pxl.basicSecurity.appDev;

import be.pxl.basicSecurity.appDev.steganography.SteganoASCII;

public class SteganographyApp {
    public static void main(String[] args) {
        boolean[] bits = SteganoASCII.encode("Dit is een testtekst.");
        String text = SteganoASCII.decode(bits);
        System.out.println(text);
    }
}
