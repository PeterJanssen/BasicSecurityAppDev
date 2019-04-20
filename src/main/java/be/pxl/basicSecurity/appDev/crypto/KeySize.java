package be.pxl.basicSecurity.appDev.crypto;


/**
 * This enum specifies the valid bit lengths for keys in
 * classes AESEncryption and RSAEncryption
 * <p>
 * NOTE: Keys of length higher than 128 bits for AES require installation
 * of Java Cryptography Extension (JCE) Unlimited Strength
 * Jurisdiction Policy Files for JDK 8 and lower.
 */

public enum KeySize {

    SIZE_128(128),
    SIZE_192(192),
    SIZE_256(256),
    SIZE_512(512),
    SIZE_1024(1024),
    SIZE_2048(2048);

    private int length;

    private KeySize(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
