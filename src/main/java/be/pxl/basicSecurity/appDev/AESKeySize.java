package be.pxl.basicSecurity.appDev;


/**
 * This enum specifies the valid bit lengths for AES keys in
 * class AESEncryption
 * <p>
 * NOTE: Keys of length higher than 128 bits require installation
 * of Java Cryptography Extension (JCE) Unlimited Strength
 * Jurisdiction Policy Files for JDK 8 and lower.
 */

public enum AESKeySize {

    SIZE_128(128),
    SIZE_192(192),
    SIZE_256(256);
    private int length;

    private AESKeySize(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}