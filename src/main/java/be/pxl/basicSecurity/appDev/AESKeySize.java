package be.pxl.basicSecurity.appDev;

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
